package services.impl.qrcodetoken

import foundation.cache.CacheHandler
import io.github.alaksion.invoicer.foundation.log.LogLevel
import io.github.alaksion.invoicer.foundation.log.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.selects.select
import models.qrcodetoken.AuthorizedQrCodeToken
import services.api.services.qrcodetoken.GetQrCodeTokenByContentIdService
import services.api.services.qrcodetoken.PollAuthorizedTokenService
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal class PollAuthorizedTokenServiceImpl(
    private val cacheHandler: CacheHandler,
    private val getTokenService: GetQrCodeTokenByContentIdService,
    private val dispatcher: CoroutineDispatcher,
    private val logger: Logger
) : PollAuthorizedTokenService {

    override suspend fun poll(
        contentId: String,
        interval: Duration,
    ): PollAuthorizedTokenService.Response {
        return coroutineScope {
            getTokenService.find(contentId)
                ?: return@coroutineScope PollAuthorizedTokenService.Response.CloseConnection("Token not found")

            val cancellation = async(dispatcher) {
                delay(60.seconds)
                logger.log(
                    type = PollAuthorizedTokenServiceImpl::class,
                    message = "Authorized QrToken timed out. Aborting connection",
                    level = LogLevel.Debug
                )
                PollAuthorizedTokenService.Response.CloseConnection("Connection timed out")
            }

            val poll = async(dispatcher) { pollToken(contentId) }

            select {
                cancellation.onAwait { it }
                poll.onAwait { it }
            }.also {
                if (cancellation.isActive) cancellation.cancel()
                if (poll.isActive) poll.cancel()
            }
        }
    }

    private suspend fun pollToken(
        contentId: String
    ): PollAuthorizedTokenService.Response {
        val authData = cacheHandler.get(key = contentId, serializer = AuthorizedQrCodeToken.serializer())
        if (authData != null) {
            logger.log(
                type = PollAuthorizedTokenServiceImpl::class,
                message = "Authorized QrToken found in cache, deleting it.",
                level = LogLevel.Debug
            )
            cacheHandler.delete(contentId)
            return PollAuthorizedTokenService.Response.Success(authData)
        } else {
            logger.log(
                type = PollAuthorizedTokenServiceImpl::class,
                message = "Authorized QrToken not found in cache, waiting 1sec before another poll.",
                level = LogLevel.Debug
            )
            delay(1.seconds)
            return pollToken(contentId)
        }
    }
}
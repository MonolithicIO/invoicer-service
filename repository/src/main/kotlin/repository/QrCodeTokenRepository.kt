package repository

import datasource.api.database.QrCodeTokenDatabaseSource
import foundation.cache.CacheHandler
import models.qrcodetoken.AuthorizedQrCodeToken
import models.qrcodetoken.QrCodeTokenModel
import java.util.*

interface QrCodeTokenRepository {
    suspend fun createQrCodeToken(
        ipAddress: String,
        agent: String,
        base64Content: String,
        content: String,
    ): QrCodeTokenModel

    suspend fun getQrCodeTokenByUUID(
        tokenId: UUID
    ): QrCodeTokenModel?

    suspend fun consumeQrCodeToken(
        tokenId: UUID
    ): QrCodeTokenModel?

    suspend fun expireQrCodeToken(
        tokenId: UUID
    )

    suspend fun getQrCodeByTokenId(
        contentId: String,
    ): QrCodeTokenModel?

    suspend fun storeAuthorizedToken(
        contentId: String,
        token: AuthorizedQrCodeToken
    )

    suspend fun getAuthorizedToken(
        contentId: String
    ): AuthorizedQrCodeToken?

    suspend fun clearAuthorizedToken(
        contentId: String
    )
}

internal class QrCodeTokenRepositoryImpl(
    private val databaseSource: QrCodeTokenDatabaseSource,
    private val cacheHandler: CacheHandler,
) : QrCodeTokenRepository {

    override suspend fun createQrCodeToken(
        ipAddress: String,
        agent: String,
        base64Content: String,
        content: String,
    ): QrCodeTokenModel {
        return databaseSource.createQrCodeToken(
            ipAddress = ipAddress,
            agent = agent,
            base64Content = base64Content,
            content = content
        )
    }

    override suspend fun getQrCodeTokenByUUID(tokenId: UUID): QrCodeTokenModel? {
        return getQrCodeTokenByUUID(tokenId = tokenId)
    }

    override suspend fun consumeQrCodeToken(tokenId: UUID): QrCodeTokenModel? {
        return databaseSource.consumeQrCodeToken(tokenId = tokenId)
    }

    override suspend fun expireQrCodeToken(tokenId: UUID) {
        databaseSource.expireQrCodeToken(tokenId = tokenId)
    }

    override suspend fun getQrCodeByTokenId(contentId: String): QrCodeTokenModel? {
        return databaseSource.getQrCodeTokenByContentId(contentId = contentId)
    }

    override suspend fun storeAuthorizedToken(
        contentId: String,
        token: AuthorizedQrCodeToken
    ) {
        cacheHandler.set(
            key = token.rawContent,
            value = AuthorizedQrCodeToken(
                refreshToken = token.refreshToken,
                accessToken = token.accessToken,
                rawContent = token.rawContent
            ),
            serializer = AuthorizedQrCodeToken.serializer(),
            ttlSeconds = CACHE_TTL_SECONDS
        )
    }

    override suspend fun getAuthorizedToken(contentId: String): AuthorizedQrCodeToken? {
        return cacheHandler.get(
            contentId,
            serializer = AuthorizedQrCodeToken.serializer()
        )
    }

    override suspend fun clearAuthorizedToken(contentId: String) {
        cacheHandler.delete(
            key = contentId,
        )
    }

    companion object {
        const val CACHE_TTL_SECONDS = 60L
    }
}
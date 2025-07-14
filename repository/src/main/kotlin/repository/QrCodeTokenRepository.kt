package repository

import foundation.cache.CacheHandler
import java.util.*
import models.qrcodetoken.AuthorizedQrCodeToken
import models.qrcodetoken.QrCodeTokenModel
import repository.datasource.QrCodeTokenDataSource

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

    suspend fun getQrCodeByContentId(
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
    private val cacheHandler: CacheHandler,
    private val qrCodeTokenDataSource: QrCodeTokenDataSource
) : QrCodeTokenRepository {

    override suspend fun createQrCodeToken(
        ipAddress: String,
        agent: String,
        base64Content: String,
        content: String,
    ): QrCodeTokenModel {
        return qrCodeTokenDataSource.createQrCodeToken(
            ipAddress = ipAddress,
            agent = agent,
            base64Content = base64Content,
            content = content
        )
    }

    override suspend fun getQrCodeTokenByUUID(tokenId: UUID): QrCodeTokenModel? {
        return qrCodeTokenDataSource.getQrCodeTokenByUUID(
            tokenId = tokenId
        )
    }

    override suspend fun consumeQrCodeToken(tokenId: UUID): QrCodeTokenModel? {
        return qrCodeTokenDataSource.consumeQrCodeToken(
            tokenId = tokenId
        )
    }

    override suspend fun expireQrCodeToken(tokenId: UUID) {
        return qrCodeTokenDataSource.expireQrCodeToken(tokenId = tokenId)
    }

    override suspend fun getQrCodeByContentId(contentId: String): QrCodeTokenModel? {
        return qrCodeTokenDataSource.getQrCodeByContentId(contentId = contentId)
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

package repository.api.repository

import datasource.api.database.QrCodeTokenDatabaseSource
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
}

internal class QrCodeTokenRepositoryImpl(
    private val databaseSource: QrCodeTokenDatabaseSource
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
}
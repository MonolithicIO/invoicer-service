package repository.api.repository

import datasource.api.database.QrCodeTokenDatabaseSource
import models.qrcodetoken.QrCodeTokenModel

interface QrCodeTokenRepository {
    suspend fun createQrCodeToken(
        ipAddress: String,
        agent: String,
        base64Content: String,
        content: String,
    ): QrCodeTokenModel

    suspend fun getQrCodeTokenByUUID(
        id: String
    ): QrCodeTokenModel?

    suspend fun consumeQrCodeToken(
        id: String
    ): QrCodeTokenModel?

    suspend fun expireQrCodeToken(
        id: String
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

    override suspend fun getQrCodeTokenByUUID(id: String): QrCodeTokenModel? {
        return getQrCodeTokenByUUID(id = id)
    }

    override suspend fun consumeQrCodeToken(id: String): QrCodeTokenModel? {
        return databaseSource.consumeQrCodeToken(id = id)
    }

    override suspend fun expireQrCodeToken(id: String) {
        databaseSource.expireQrCodeToken(id = id)
    }

    override suspend fun getQrCodeByTokenId(contentId: String): QrCodeTokenModel? {
        return databaseSource.getQrCodeTokenByContentId(contentId = contentId)
    }
}
package repository.api.repository

import datasource.api.database.QrCodeTokenDatabaseSource
import models.qrcodetoken.QrCodeTokenModel

interface QrCodeTokenRepository {
    suspend fun createQrCodeToken(
        ipAddress: String,
        agent: String,
        base64Content: String,
    ): String

    suspend fun getQrCodeTokenByUUID(
        id: String
    ): QrCodeTokenModel?

    suspend fun consumeQrCodeToken(
        id: String
    ): QrCodeTokenModel?

    suspend fun expireQrCodeToken(
        id: String
    )
}

internal class QrCodeTokenRepositoryImpl(
    private val databaseSource: QrCodeTokenDatabaseSource
) : QrCodeTokenRepository {

    override suspend fun createQrCodeToken(ipAddress: String, agent: String, base64Content: String): String {
        return databaseSource.createQrCodeToken(
            ipAddress = ipAddress,
            agent = agent,
            base64Content = base64Content
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
}
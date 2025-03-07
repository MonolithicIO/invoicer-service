package datasource.api.database

import models.qrcodetoken.QrCodeTokenModel

interface QrCodeTokenDatabaseSource {
    suspend fun createQrCodeToken(
        ipAddress: String,
        agent: String,
        base64Content: String,
        content: String,
    ): QrCodeTokenModel

    suspend fun getQrCodeTokenByContentId(
        contentId: String
    ): QrCodeTokenModel?

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
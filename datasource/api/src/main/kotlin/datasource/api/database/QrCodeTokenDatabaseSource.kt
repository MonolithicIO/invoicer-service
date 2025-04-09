package datasource.api.database

import models.qrcodetoken.QrCodeTokenModel
import java.util.*

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
        tokenId: UUID
    ): QrCodeTokenModel?

    suspend fun consumeQrCodeToken(
        tokenId: UUID
    ): QrCodeTokenModel?

    suspend fun expireQrCodeToken(
        tokenId: UUID
    )
}
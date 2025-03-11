package services.api.services.qrcodetoken

import models.qrcodetoken.QrCodeTokenModel

interface FindQrCodeTokenByContentIdService {
    suspend fun find(contentId: String): QrCodeTokenModel?
}
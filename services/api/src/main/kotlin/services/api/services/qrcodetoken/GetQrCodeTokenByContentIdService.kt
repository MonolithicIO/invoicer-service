package services.api.services.qrcodetoken

import models.qrcodetoken.QrCodeTokenModel

interface GetQrCodeTokenByContentIdService {
    suspend fun find(contentId: String): QrCodeTokenModel?
}

package services.api.fakes.qrcode

import models.qrcodetoken.QrCodeTokenModel
import services.api.services.qrcodetoken.GetQrCodeTokenByContentIdService

class FakeGetQrCodeTokenByContentIdService : GetQrCodeTokenByContentIdService {

    var response: QrCodeTokenModel? = null

    override suspend fun find(contentId: String): QrCodeTokenModel? {
        return response
    }
}
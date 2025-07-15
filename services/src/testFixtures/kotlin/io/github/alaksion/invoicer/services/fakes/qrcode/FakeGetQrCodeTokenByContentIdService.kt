package io.github.alaksion.invoicer.services.fakes.qrcode

import io.github.alaksion.invoicer.services.qrcodetoken.GetQrCodeTokenByContentIdService
import models.qrcodetoken.QrCodeTokenModel

class FakeGetQrCodeTokenByContentIdService : GetQrCodeTokenByContentIdService {

    var response: QrCodeTokenModel? = null

    override suspend fun find(contentId: String): QrCodeTokenModel? {
        return response
    }
}
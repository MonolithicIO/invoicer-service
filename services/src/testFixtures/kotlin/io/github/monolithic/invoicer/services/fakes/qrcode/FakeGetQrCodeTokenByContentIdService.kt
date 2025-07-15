package io.github.monolithic.invoicer.services.fakes.qrcode

import io.github.monolithic.invoicer.services.qrcodetoken.GetQrCodeTokenByContentIdService
import io.github.monolithic.invoicer.models.qrcodetoken.QrCodeTokenModel

class FakeGetQrCodeTokenByContentIdService : GetQrCodeTokenByContentIdService {

    var response: QrCodeTokenModel? = null

    override suspend fun find(contentId: String): QrCodeTokenModel? {
        return response
    }
}
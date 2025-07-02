package services.api.services.qrcodetoken

import models.login.RequestLoginCodeModel
import models.qrcodetoken.QrCodeTokenModel

interface RequestQrCodeTokenService {
    suspend fun requestQrCodeToken(
        request: RequestLoginCodeModel
    ): QrCodeTokenModel
}

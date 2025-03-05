package services.impl.login

import foundation.qrcode.QrCodeGenerator
import models.login.LoginCodeModel
import models.login.RequestLoginCodeModel
import services.api.services.login.RequestLoginCodeService

internal class RequestLoginCodeServiceImpl(
    private val qrCodeGenerator: QrCodeGenerator,
) : RequestLoginCodeService {

    override suspend fun requestLoginCode(
        model: RequestLoginCodeModel
    ): LoginCodeModel {

        val qrCode = qrCodeGenerator.generateBase64QrCode(
            text = "",
            size = model.size
        )


        return LoginCodeModel(
            encodedBase64 = qrCode
        )
    }
}
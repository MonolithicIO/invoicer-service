package services.impl.qrcodetoken

import foundation.qrcode.QrCodeGenerator
import models.login.RequestLoginCodeModel
import models.qrcodetoken.QrCodeTokenModel
import repository.api.repository.QrCodeTokenRepository
import services.api.services.qrcodetoken.RequestQrCodeTokenService
import utils.exceptions.http.badRequestError
import java.util.*

internal class RequestQrCodeTokenServiceImpl(
    private val qrCodeGenerator: QrCodeGenerator,
    private val qrCodeTokenRepository: QrCodeTokenRepository
) : RequestQrCodeTokenService {

    override suspend fun requestQrCodeToken(
        request: RequestLoginCodeModel
    ): QrCodeTokenModel {

        if (request.size < 0) {
            badRequestError("Size must be greater than 0")
        }

        val qrCodeContent = UUID.randomUUID().toString()

        val base64Content = qrCodeGenerator.generateBase64QrCode(
            text = qrCodeContent,
            size = request.size
        )

        return qrCodeTokenRepository.createQrCodeToken(
            ipAddress = request.ipAddress,
            agent = request.agent,
            content = qrCodeContent,
            base64Content = base64Content
        )
    }
}

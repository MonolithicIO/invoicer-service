package io.github.alaksion.invoicer.services.qrcodetoken

import foundation.qrcode.QrCodeGenerator
import java.util.UUID
import models.login.RequestLoginCodeModel
import models.qrcodetoken.QrCodeTokenModel
import repository.QrCodeTokenRepository
import utils.exceptions.http.badRequestError

interface RequestQrCodeTokenService {
    suspend fun requestQrCodeToken(
        request: RequestLoginCodeModel
    ): QrCodeTokenModel
}

internal class RequestQrCodeTokenServiceImpl(
    private val qrCodeGenerator: QrCodeGenerator,
    private val qrCodeTokenRepository: QrCodeTokenRepository
) : RequestQrCodeTokenService {

    override suspend fun requestQrCodeToken(
        request: RequestLoginCodeModel
    ): QrCodeTokenModel {

        if (request.size < 1) {
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
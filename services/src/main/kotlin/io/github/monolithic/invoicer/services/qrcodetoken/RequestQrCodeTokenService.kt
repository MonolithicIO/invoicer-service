package io.github.monolithic.invoicer.services.qrcodetoken

import io.github.monolithic.invoicer.foundation.qrcode.QrCodeGenerator
import java.util.UUID
import io.github.monolithic.invoicer.models.login.RequestLoginCodeModel
import io.github.monolithic.invoicer.models.qrcodetoken.QrCodeTokenModel
import io.github.monolithic.invoicer.repository.QrCodeTokenRepository
import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError

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

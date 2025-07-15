package io.github.alaksion.invoicer.services.qrcodetoken

import models.qrcodetoken.QrCodeTokenModel
import repository.QrCodeTokenRepository

interface GetQrCodeTokenByContentIdService {
    suspend fun find(contentId: String): QrCodeTokenModel?
}

internal class GetQrCodeTokenByContentIdServiceImpl(
    private val qrCodeTokenRepository: QrCodeTokenRepository
) : GetQrCodeTokenByContentIdService {
    override suspend fun find(contentId: String): QrCodeTokenModel? {
        return qrCodeTokenRepository.getQrCodeByContentId(contentId = contentId)
    }
}

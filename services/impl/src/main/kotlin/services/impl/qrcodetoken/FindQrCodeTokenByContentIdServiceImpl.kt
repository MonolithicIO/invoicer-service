package services.impl.qrcodetoken

import models.qrcodetoken.QrCodeTokenModel
import repository.api.repository.QrCodeTokenRepository
import services.api.services.qrcodetoken.FindQrCodeTokenByContentIdService

internal class FindQrCodeTokenByContentIdServiceImpl(
    private val qrCodeTokenRepository: QrCodeTokenRepository
) : FindQrCodeTokenByContentIdService {
    override suspend fun find(contentId: String): QrCodeTokenModel? {
        return qrCodeTokenRepository.getQrCodeByTokenId(contentId = contentId)
    }
}
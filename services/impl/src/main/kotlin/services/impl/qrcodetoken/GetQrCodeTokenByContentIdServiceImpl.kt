package services.impl.qrcodetoken

import models.qrcodetoken.QrCodeTokenModel
import repository.QrCodeTokenRepository
import services.api.services.qrcodetoken.GetQrCodeTokenByContentIdService

internal class GetQrCodeTokenByContentIdServiceImpl(
    private val qrCodeTokenRepository: QrCodeTokenRepository
) : GetQrCodeTokenByContentIdService {
    override suspend fun find(contentId: String): QrCodeTokenModel? {
        return qrCodeTokenRepository.getQrCodeByContentId(contentId = contentId)
    }
}

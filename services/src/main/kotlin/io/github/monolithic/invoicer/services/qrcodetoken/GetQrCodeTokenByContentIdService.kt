package io.github.monolithic.invoicer.services.qrcodetoken

import io.github.monolithic.invoicer.models.qrcodetoken.QrCodeTokenModel
import io.github.monolithic.invoicer.repository.QrCodeTokenRepository

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

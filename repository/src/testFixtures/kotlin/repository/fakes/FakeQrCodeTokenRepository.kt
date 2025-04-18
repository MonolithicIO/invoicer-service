package repository.fakes

import models.fixtures.qrCodeTokenModelFixture
import models.qrcodetoken.AuthorizedQrCodeToken
import models.qrcodetoken.QrCodeTokenModel
import repository.QrCodeTokenRepository
import java.util.*

class FakeQrCodeTokenRepository : QrCodeTokenRepository {

    var createQrCodeTokenResponse: suspend () -> QrCodeTokenModel = { qrCodeTokenModelFixture }
    var getQrCodeTokenByUUIDResponse: suspend () -> QrCodeTokenModel? = { null }
    var consumeQrCodeTokenResponse: suspend () -> QrCodeTokenModel? = { null }
    var getQrCodeTokenByIdResponse: suspend () -> QrCodeTokenModel? = { null }
    var getAuthorizedQrCodeToken: suspend () -> AuthorizedQrCodeToken? = { null }

    var consumeCalls = 0
        private set

    val storeAuthorizedTokenCallstack = mutableListOf<Pair<String, AuthorizedQrCodeToken>>()
    val getByContentIdCallstack = mutableListOf<String>()

    override suspend fun createQrCodeToken(
        ipAddress: String,
        agent: String,
        base64Content: String,
        content: String
    ): QrCodeTokenModel {
        return createQrCodeTokenResponse()
    }

    override suspend fun getQrCodeTokenByUUID(tokenId: UUID): QrCodeTokenModel? {
        return getQrCodeTokenByUUIDResponse()
    }

    override suspend fun consumeQrCodeToken(tokenId: UUID): QrCodeTokenModel? {
        consumeCalls++
        return consumeQrCodeTokenResponse()
    }

    override suspend fun expireQrCodeToken(tokenId: UUID) = Unit

    override suspend fun getQrCodeByTokenId(contentId: String): QrCodeTokenModel? {
        getByContentIdCallstack.add(contentId)
        return getQrCodeTokenByIdResponse()
    }

    override suspend fun storeAuthorizedToken(contentId: String, token: AuthorizedQrCodeToken) {
        storeAuthorizedTokenCallstack.add(Pair(contentId, token))
    }

    override suspend fun getAuthorizedToken(contentId: String): AuthorizedQrCodeToken? {
        return getAuthorizedQrCodeToken()
    }

    override suspend fun clearAuthorizedToken(contentId: String) = Unit
}
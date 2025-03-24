package datasource.api.database

import models.qrcodetoken.QrCodeTokenModel
import models.fixtures.qrCodeTokenModelFixture

class FakeQrCodeTokenDatabaseSource : QrCodeTokenDatabaseSource {

    var createQrCodeTokenResponse: suspend () -> QrCodeTokenModel = { qrCodeTokenModelFixture }
    var getQrCodeTokenByContentIdResponse: suspend () -> QrCodeTokenModel? = { qrCodeTokenModelFixture }
    var getQrCodeTokenByUUIDResponse: suspend () -> QrCodeTokenModel? = { qrCodeTokenModelFixture }
    var consumeQrCodeTokenResponse: suspend () -> QrCodeTokenModel? = { qrCodeTokenModelFixture }
    var expireQrCodeTokenResponse: suspend () -> Unit = {}

    override suspend fun createQrCodeToken(
        ipAddress: String,
        agent: String,
        base64Content: String,
        content: String
    ): QrCodeTokenModel {
        return createQrCodeTokenResponse()
    }

    override suspend fun getQrCodeTokenByContentId(contentId: String): QrCodeTokenModel? {
        return getQrCodeTokenByContentIdResponse()
    }

    override suspend fun getQrCodeTokenByUUID(id: String): QrCodeTokenModel? {
        return getQrCodeTokenByUUIDResponse()
    }

    override suspend fun consumeQrCodeToken(id: String): QrCodeTokenModel? {
        return consumeQrCodeTokenResponse()
    }

    override suspend fun expireQrCodeToken(id: String) {
        return expireQrCodeTokenResponse()
    }
}
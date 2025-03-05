package services.api.services.login

import models.login.LoginCodeModel
import models.login.RequestLoginCodeModel

interface RequestLoginCodeService {
    suspend fun requestLoginCode(model: RequestLoginCodeModel): LoginCodeModel
}
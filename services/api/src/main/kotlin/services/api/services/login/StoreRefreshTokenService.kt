package services.api.services.login

import java.util.UUID

interface StoreRefreshTokenService {
    suspend fun storeRefreshToken(token: String, userId: UUID)
}

package services.api.services.login

interface StoreRefreshTokenService {
    suspend fun storeRefreshToken(token: String, userId: String)
}
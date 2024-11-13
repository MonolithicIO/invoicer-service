package services.impl.login

import foundation.authentication.api.FakeAuthTokenManager
import foundation.authentication.api.jwt.InvoicerJwtVerifier
import repository.api.repository.RefreshTokenRepository
import services.impl.refreshtoken.FakeStoreRefreshTokenService
import services.test.user.FakeGetUserByIdService

class RefreshLoginServiceImplTest {

    private lateinit var service: RefreshLoginServiceImpl

    private lateinit var invoicerJwtVerifier: InvoicerJwtVerifier
    private lateinit var tokenManager: FakeAuthTokenManager
    private lateinit var getUserByIdService: FakeGetUserByIdService
    private lateinit var refreshTokenRepository: RefreshTokenRepository
    private lateinit var storeRefreshTokenService: FakeStoreRefreshTokenService
}
package services.impl.login

import foundation.authentication.impl.AuthTokenManager
import foundation.password.PasswordEncryption
import io.github.alaksion.foundation.identity.provider.IdentityProvider
import io.github.alaksion.foundation.identity.provider.IdentityProviderResult
import io.github.alaksion.invoicer.utils.uuid.parseUuid
import models.login.AuthTokenModel
import models.user.CreateUserModel
import models.user.UserModel
import repository.UserRepository
import services.api.services.login.GoogleLoginService
import services.api.services.login.StoreRefreshTokenService
import services.api.services.user.GetUserByEmailService
import utils.exceptions.http.badRequestError
import utils.exceptions.http.conflictError
import java.util.*

internal class GoogleLoginServiceImpl(
    private val identityProvider: IdentityProvider,
    private val getUserByEmailServiceImpl: GetUserByEmailService,
    private val userRepository: UserRepository,
    private val authTokenManager: AuthTokenManager,
    private val storeRefreshTokenService: StoreRefreshTokenService,
    private val passwordEncryption: PasswordEncryption
) : GoogleLoginService {
    override suspend fun login(token: String): AuthTokenModel {

        return when (val providerUser = identityProvider.getGoogleIdentity(token)) {
            is IdentityProviderResult.Error -> badRequestError(message = providerUser.error.toString())
            is IdentityProviderResult.Success -> handleUserFlow(account = providerUser)
        }
    }

    private suspend fun handleUserFlow(
        account: IdentityProviderResult.Success
    ): AuthTokenModel {
        val existingUser = getUserByEmailServiceImpl.get(email = account.email)

        return existingUser?.let { user ->
            handleExistingUserFlow(
                user = user,
                identityProviderUuid = account.providerUuid
            )
        } ?: handleNewUserFlow(
            email = account.email,
            providerUuid = account.providerUuid
        )
    }

    private suspend fun handleNewUserFlow(
        email: String,
        providerUuid: String
    ): AuthTokenModel {
        val userId = createUser(email = email, providerUuid = providerUuid)
        return generateTokens(userId)
    }

    private suspend fun handleExistingUserFlow(
        user: UserModel,
        identityProviderUuid: String
    ): AuthTokenModel {
        if (user.identityProviderUuid != identityProviderUuid) {
            conflictError(message = "Google account is already linked to another user.")
        }
        return generateTokens(userUuid = user.id)
    }

    private suspend fun generateTokens(
        userUuid: UUID
    ): AuthTokenModel {
        val refreshToken = authTokenManager.generateRefreshToken(userUuid.toString())
        val accessToken = authTokenManager.generateToken(userUuid.toString())

        storeRefreshTokenService.storeRefreshToken(
            token = refreshToken,
            userId = userUuid
        )

        return AuthTokenModel(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    private suspend fun createUser(
        email: String,
        providerUuid: String
    ): UUID {
        return parseUuid(
            userRepository.createUser(
                CreateUserModel(
                    email = email,
                    confirmEmail = email,
                    identityProviderUuid = providerUuid,
                    password = passwordEncryption.encryptPassword(UUID.randomUUID().toString())
                )
            )
        )
    }
}
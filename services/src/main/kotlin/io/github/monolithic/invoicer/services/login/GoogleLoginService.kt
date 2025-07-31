package io.github.monolithic.invoicer.services.login

import io.github.monolithic.invoicer.foundation.password.PasswordEncryption
import io.github.monolithic.invoicer.foundation.authentication.provider.IdentityProvider
import io.github.monolithic.invoicer.foundation.authentication.provider.IdentityProviderResult
import io.github.monolithic.invoicer.foundation.authentication.token.AuthTokenManager
import io.github.monolithic.invoicer.utils.uuid.parseUuid
import java.util.UUID
import io.github.monolithic.invoicer.models.login.AuthTokenModel
import io.github.monolithic.invoicer.models.user.CreateUserModel
import io.github.monolithic.invoicer.models.user.UserModel
import io.github.monolithic.invoicer.repository.UserRepository
import io.github.monolithic.invoicer.services.user.GetUserByEmailService
import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError
import io.github.monolithic.invoicer.foundation.exceptions.http.conflictError

interface GoogleLoginService {
    suspend fun login(token: String): AuthTokenModel
}

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
        val refreshToken = authTokenManager.generateRefreshToken()
        val accessToken = authTokenManager.generateToken(userUuid.toString())

        storeRefreshTokenService.createRefreshToken(
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

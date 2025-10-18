package io.github.monolithic.invoicer.services.user

import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError
import io.github.monolithic.invoicer.foundation.log.LogLevel
import io.github.monolithic.invoicer.foundation.log.Logger
import io.github.monolithic.invoicer.repository.PasswordResetRepository
import io.github.monolithic.invoicer.utils.uuid.UuidProvider
import java.util.*
import kotlinx.datetime.Clock

typealias ResetPasswordToken = String

interface VerifyResetPasswordRequestService {
    suspend fun consume(
        pinCode: String,
        requestId: UUID
    ): ResetPasswordToken
}

internal class VerifyResetPasswordRequestServiceImpl(
    private val passwordResetRepository: PasswordResetRepository,
    private val getUserByIdService: GetUserByIdService,
    private val clock: Clock,
    private val uuidProvider: UuidProvider,
    private val logger: Logger
) : VerifyResetPasswordRequestService {

    override suspend fun consume(
        pinCode: String,
        requestId: UUID
    ): ResetPasswordToken {
        val passwordRequest =
            passwordResetRepository.getPasswordResetRequestById(id = requestId)

        if (passwordRequest == null) {
            logger.log(
                type = VerifyResetPasswordRequestServiceImpl::class,
                message = "Password reset request with id $requestId not found.",
                level = LogLevel.Debug
            )
            badRequestError(
                message = ERROR_MESSAGE
            )
        }

        if (passwordRequest.attempts >= MAX_ATTEMPTS) {
            badRequestError(message = "Maximum number of attempts exceeded for reset password request.")
        }

        val user = getUserByIdService.get(passwordRequest.userId)

        if (passwordRequest.isConsumed || clock.now() > passwordRequest.expiresAt) {
            logger.log(
                type = VerifyResetPasswordRequestServiceImpl::class,
                message = "Password reset request with id $requestId for user ${user.id}is expired.",
                level = LogLevel.Debug
            )
            badRequestError(message = ERROR_MESSAGE)
        }

        if (passwordRequest.safeCode != pinCode) {
            logger.log(
                type = VerifyResetPasswordRequestServiceImpl::class,
                message = "Password reset request with id $requestId for user ${user.id} is wrong pin code.",
                level = LogLevel.Debug
            )
            badRequestError(message = ERROR_MESSAGE)
        }

        passwordResetRepository.consumePasswordResetRequest(passwordRequest.id)

        val resetToken = uuidProvider.generateUuid()

        passwordResetRepository.storeResetToken(
            token = resetToken,
            userId = passwordRequest.userId
        )

        return resetToken
    }

    companion object {
        const val ERROR_MESSAGE = "Could not validate reset password request. PIN code is invalid or has expired."
        const val MAX_ATTEMPTS = 3
    }

}

package io.github.monolithic.invoicer.services.user

import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError
import io.github.monolithic.invoicer.repository.PasswordResetRepository
import io.github.monolithic.invoicer.utils.uuid.UuidProvider
import java.util.*
import kotlinx.datetime.Clock

typealias ResetPasswordToken = String

interface ConsumeResetPasswordRequestService {
    suspend fun consume(
        pinCode: String,
        requestId: UUID
    ): ResetPasswordToken
}

internal class ConsumeResetPasswordRequestServiceImpl(
    private val passwordResetRepository: PasswordResetRepository,
    private val getUserByIdService: GetUserByIdService,
    private val clock: Clock,
    private val uuidProvider: UuidProvider
) : ConsumeResetPasswordRequestService {

    override suspend fun consume(
        pinCode: String,
        requestId: UUID
    ): ResetPasswordToken {
        val passwordRequest =
            passwordResetRepository.getPasswordResetRequestById(id = requestId)
                ?: badRequestError(message = "Could not validate reset password request. PIN code is invalid or has expired.")

        getUserByIdService.get(passwordRequest.userId)

        if (passwordRequest.isConsumed || clock.now() > passwordRequest.expiresAt) {
            badRequestError(message = "Could not validate reset password request. PIN code is invalid or has expired.")
        }

        if (passwordRequest.safeCode != pinCode) {
            badRequestError(message = "Could not validate reset password request. PIN code is invalid or has expired.")
        }

        passwordResetRepository.consumePasswordResetRequest(passwordRequest.id)

        val resetToken = uuidProvider.generateUuid()

        passwordResetRepository.storeResetToken(
            token = resetToken,
            userId = passwordRequest.userId
        )

        return resetToken
    }

}

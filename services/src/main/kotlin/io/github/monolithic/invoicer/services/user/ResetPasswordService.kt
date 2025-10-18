package io.github.monolithic.invoicer.services.user

import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError
import io.github.monolithic.invoicer.foundation.messaging.MessageProducer
import io.github.monolithic.invoicer.foundation.messaging.MessageTopic
import io.github.monolithic.invoicer.foundation.password.PasswordEncryption
import io.github.monolithic.invoicer.foundation.password.PasswordStrength
import io.github.monolithic.invoicer.foundation.password.PasswordValidator
import io.github.monolithic.invoicer.repository.PasswordResetRepository
import io.github.monolithic.invoicer.repository.UserRepository
import io.github.monolithic.invoicer.utils.uuid.parseUuid
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

interface ResetPasswordService {
    suspend fun reset(
        token: String,
        newPassword: String,
        confirmNewPassword: String
    )
}

internal class ResetPasswordServiceImpl(
    private val resetPasswordResetRepository: PasswordResetRepository,
    private val getUserByIdService: GetUserByIdService,
    private val passwordEncryption: PasswordEncryption,
    private val passwordValidator: PasswordValidator,
    private val userRepository: UserRepository,
    private val messageProducer: MessageProducer
) : ResetPasswordService {

    override suspend fun reset(token: String, newPassword: String, confirmNewPassword: String) {
        val resetToken = resetPasswordResetRepository.getResetToken(
            token = token
        ) ?: badRequestError(message = "Invalid or expired reset password token.")

        val user = getUserByIdService.get(parseUuid(resetToken.userId))

        if (newPassword != confirmNewPassword) {
            badRequestError(message = "New password and confirmation do not match.")
        }

        if (passwordValidator.validate(newPassword) is PasswordStrength.WEAK) {
            badRequestError(message = "New password does not meet security requirements.")
        }

        val encryptedPassword = passwordEncryption.encryptPassword(
            rawPassword = newPassword
        )

        userRepository.updatePassword(
            id = user.id,
            newPassword = encryptedPassword
        )

        resetPasswordResetRepository.clearResetToken(token = token)

        messageProducer.produceMessage(
            topic = MessageTopic.EMAILS,
            key = "password-reset-completed-${token}",
            value = JsonObject(
                mapOf(
                    "type" to JsonPrimitive("send_reset_password_completed_email"),
                    "email" to JsonPrimitive(user.email),
                )
            ).toString()
        )
    }
}

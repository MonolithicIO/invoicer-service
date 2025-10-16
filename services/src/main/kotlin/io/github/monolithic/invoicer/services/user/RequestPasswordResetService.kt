package io.github.monolithic.invoicer.services.user

import io.github.monolithic.invoicer.foundation.messaging.MessageProducer
import io.github.monolithic.invoicer.foundation.messaging.MessageTopic
import io.github.monolithic.invoicer.models.resetpassword.CreateResetPasswordRequestModel
import io.github.monolithic.invoicer.repository.PasswordResetRepository
import io.github.monolithic.invoicer.utils.code.SixDigitGenerator
import io.github.monolithic.invoicer.utils.uuid.UuidProvider
import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.Clock
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

typealias RequestPasswordToken = String

interface RequestPasswordResetService {
    suspend fun requestReset(email: String): RequestPasswordToken
}

internal class RequestPasswordResetServiceImpl(
    private val uuidProvider: UuidProvider,
    private val getUserByEmailService: GetUserByEmailService,
    private val codeGenerator: SixDigitGenerator,
    private val clock: Clock,
    private val messageProducer: MessageProducer,
    private val passwordResetRepository: PasswordResetRepository

) : RequestPasswordResetService {
    override suspend fun requestReset(email: String): RequestPasswordToken {

        // User enumeration safeguard
        val user = getUserByEmailService.get(email) ?: return uuidProvider.generateUuid()

        val request = CreateResetPasswordRequestModel(
            safeCode = codeGenerator.generateSixDigitCode(),
            userId = user.id,
            expiresAt = clock.now().plus(CODE_EXPIRATION),
            expirationText = "${CODE_EXPIRATION.inWholeMinutes} minutes",
        )

        val requestToken = passwordResetRepository.createPasswordResetRequest(
            request = request
        )

        messageProducer.produceMessage(
            topic = MessageTopic.EMAILS,
            key = "reset-password-${requestToken}",
            value = JsonObject(
                content = mapOf(
                    "type" to JsonPrimitive("send_reset_password_email"),
                    "token" to JsonPrimitive(requestToken),
                )
            ).toString()
        )

        return requestToken
    }

    companion object {
        val CODE_EXPIRATION = 15.minutes
    }
}

package io.github.monolithic.invoicer.services.user

import io.github.monolithic.invoicer.foundation.email.EmailSender
import io.github.monolithic.invoicer.foundation.email.templates.EmailTemplates
import io.github.monolithic.invoicer.foundation.log.LogLevel
import io.github.monolithic.invoicer.foundation.log.Logger
import io.github.monolithic.invoicer.repository.PasswordResetRepository
import io.github.monolithic.invoicer.utils.uuid.parseUuid
import kotlinx.datetime.Clock

interface SendRestPasswordEmailService {
    suspend fun send(resetRequestId: String)
}

internal class SendRestPasswordEmailServiceImpl(
    private val emailSender: EmailSender,
    private val resetPasswordRepository: PasswordResetRepository,
    private val getUserByIdService: GetUserByIdService,
    private val clock: Clock,
    private val logger: Logger
) : SendRestPasswordEmailService {

    override suspend fun send(resetRequestId: String) {
        val resetRequest = resetPasswordRepository.getPasswordResetRequestById(id = parseUuid(resetRequestId))

        if (resetRequest == null) {
            logger.log(
                type = SendRestPasswordEmailServiceImpl::class,
                message = "Reset password not found for token: $resetRequestId. Skipping email sending.",
                level = LogLevel.Debug
            )
            return
        }
        if (resetRequest.expiresAt < clock.now() || resetRequest.isConsumed) {
            logger.log(
                type = SendRestPasswordEmailServiceImpl::class,
                message = "Reset password request is expired for token: $resetRequestId. Skipping email sending.",
                level = LogLevel.Debug
            )
            return
        }


        val user = getUserByIdService.get(resetRequest.userId)

        emailSender.sendTemplateEmail(
            template = EmailTemplates.forgotPassword(
                recoveryCode = resetRequest.safeCode,
                expirationText = resetRequest.expirationText
            ),
            to = user.email,
            subject = "Invoicer Password Recovery",
        )
    }
}

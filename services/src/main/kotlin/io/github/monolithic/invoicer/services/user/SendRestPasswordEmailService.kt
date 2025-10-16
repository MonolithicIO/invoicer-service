package io.github.monolithic.invoicer.services.user

import io.github.monolithic.invoicer.foundation.email.EmailSender
import io.github.monolithic.invoicer.foundation.email.templates.EmailTemplates
import io.github.monolithic.invoicer.foundation.log.LogLevel
import io.github.monolithic.invoicer.foundation.log.Logger
import io.github.monolithic.invoicer.repository.PasswordResetRepository
import kotlinx.datetime.Clock

interface SendRestPasswordEmailService {
    suspend fun send(resetToken: String)
}

internal class SendRestPasswordEmailServiceImpl(
    private val emailSender: EmailSender,
    private val resetPasswordRepository: PasswordResetRepository,
    private val getUserByIdService: GetUserByIdService,
    private val clock: Clock,
    private val logger: Logger
) : SendRestPasswordEmailService {

    override suspend fun send(resetToken: String) {
        val resetRequest = resetPasswordRepository.getPasswordResetRequestByToken(token = resetToken)

        if (resetRequest == null) {
            logger.log(
                type = SendRestPasswordEmailServiceImpl::class,
                message = "Reset password not found for token: $resetToken. Skipping email sending.",
                level = LogLevel.Debug
            )
            return
        }
        if (resetRequest.expiresAt < clock.now() || resetRequest.isConsumed) {
            logger.log(
                type = SendRestPasswordEmailServiceImpl::class,
                message = "Reset password request is expired for token: $resetToken. Skipping email sending.",
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
package io.github.monolithic.invoicer.consumers.messages

internal enum class MessageType(
    val messageId: String,
) {
    INVOICE_PDF_GENERATE("invoice_generate_pdf"),
    RESET_PASSWORD_EMAIL("send_reset_password_email"),
}

package io.github.monolithic.invoicer.consumers.messages

internal enum class MessageType(
    val messageId: String,
) {
    GENERATE_PDF("generate_pdf"),
    SEND_PDF_EMAIL("send_pdf_email"),
}

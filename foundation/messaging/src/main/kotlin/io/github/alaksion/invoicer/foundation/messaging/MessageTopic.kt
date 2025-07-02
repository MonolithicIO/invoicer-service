package io.github.alaksion.invoicer.foundation.messaging

enum class MessageTopic(internal val topicId: String) {
    INVOICE_PDF("invoice.pdf.generate"),
}

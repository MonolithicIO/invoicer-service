package io.github.monolithic.invoicer.foundation.messaging

enum class MessageTopic(internal val topicId: String) {
    INVOICE_PDF("invoice.pdf.generate"),
}

package io.github.monolithic.invoicer.controller.viewmodel.invoice

import kotlinx.serialization.Serializable

@Serializable
internal data class InvoiceDownloadLinkViewModel(
    val link: String
)

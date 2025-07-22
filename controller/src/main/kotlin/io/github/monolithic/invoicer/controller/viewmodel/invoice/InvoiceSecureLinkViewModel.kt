package io.github.monolithic.invoicer.controller.viewmodel.invoice

import kotlinx.serialization.Serializable

@Serializable
internal data class InvoiceSecureLinkViewModel(
    val link: String
)

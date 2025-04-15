package controller.viewmodel.invoice

import kotlinx.serialization.Serializable

@Serializable
internal data class InvoiceDownloadLinkViewModel(
    val link: String
)

package entities

data class InvoiceListEntity(
    val items: List<InvoiceEntity>,
    val totalResults: Long,
    val nextPage: Long?
)

package datasource.api.model.pdf

data class CreatePdfData(
    val invoiceId: String,
    val pdfPath: String,
)

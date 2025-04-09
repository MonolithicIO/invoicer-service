package datasource.api.model.pdf

import java.util.*

data class CreatePdfData(
    val invoiceId: UUID,
    val pdfPath: String,
)

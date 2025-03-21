package services.impl.pdf.pdfwriter.components

import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue

internal fun buildHeader(
    senderCompanyName: String,
    senderCompanyAddress: String,
    externalId: String,
    id: String,
    dueDate: String,
    issueDate: String,
): Table {
    val table = Table(UnitValue.createPercentArray(floatArrayOf(50f, 50f)))
        .apply {
            width = UnitValue.createPercentValue(100f)
        }

    table.addCell(
        logoCell(
            senderCompanyName = senderCompanyName,
            senderCompanyAddress = senderCompanyAddress,
        )
    )
    table.addCell(
        detailsCell(
            externalId = externalId,
            id = id,
            dueDate = dueDate,
            issueDate = issueDate,
        )
    )
    return table
}

private fun logoCell(
    senderCompanyName: String,
    senderCompanyAddress: String
) = Cell()
    .add(
        Paragraph("INVOICE")
            .setFont(PdfStyle.Font.Bold)
            .setFontSize(PdfStyle.FontSize.XLarge)
            .setFontColor(PdfStyle.Color.Primary)
    )
    .add(Paragraph(senderCompanyName).setFont(PdfStyle.Font.Bold).setFontSize(PdfStyle.FontSize.Medium))
    .add(Paragraph(senderCompanyAddress).setFont(PdfStyle.Font.Regular).setFontSize(PdfStyle.FontSize.Small))
    .setBorder(null)

private fun detailsCell(
    externalId: String,
    id: String,
    dueDate: String,
    issueDate: String
) = Cell()
    .add(
        Paragraph("Nº: $externalId").setFont(PdfStyle.Font.Bold).setFontSize(PdfStyle.FontSize.Small)
            .setTextAlignment(TextAlignment.RIGHT)
    )
    .add(
        Paragraph("ID: $id").setFont(PdfStyle.Font.Regular).setFontSize(PdfStyle.FontSize.XSmall)
            .setTextAlignment(TextAlignment.RIGHT)
    )
    .add(
        Paragraph("Issue Date: $issueDate").setFont(PdfStyle.Font.Regular).setFontSize(PdfStyle.FontSize.Small)
            .setTextAlignment(TextAlignment.RIGHT)
    )
    .add(
        Paragraph("Due Date: $dueDate").setFont(PdfStyle.Font.Regular).setFontSize(PdfStyle.FontSize.Small)
            .setTextAlignment(TextAlignment.RIGHT)
    )
    .setBorder(null)
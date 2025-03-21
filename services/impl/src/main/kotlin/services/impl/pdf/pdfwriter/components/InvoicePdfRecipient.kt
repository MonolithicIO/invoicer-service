package services.impl.pdf.pdfwriter.components

import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.UnitValue

internal fun invoicePdfRecipient(
    recipientCompanyName: String,
    recipientCompanyAddress: String
): Table {
    val recipientTable = Table(1).apply {
        width = UnitValue.createPercentValue(100f)
    }

    recipientTable.addCell(
        recipientCell(
            recipientCompanyName = recipientCompanyName,
            recipientCompanyAddress = recipientCompanyAddress,
        )
    )
    return recipientTable
}

private fun recipientCell(
    recipientCompanyName: String,
    recipientCompanyAddress: String
) =
    Cell()
        .add(
            Paragraph("INVOICE FOR")
                .setFont(PdfStyle.Font.Bold)
                .setFontSize(PdfStyle.FontSize.Small)
                .setFontColor(PdfStyle.Color.Primary)
        )
        .add(Paragraph(recipientCompanyName).setFont(PdfStyle.Font.Bold).setFontSize(PdfStyle.FontSize.Medium))
        .add(Paragraph(recipientCompanyAddress).setFont(PdfStyle.Font.Regular).setFontSize(PdfStyle.FontSize.Small))
        .setBorder(null)

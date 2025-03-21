package services.impl.pdf.pdfwriter.components

import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.UnitValue

internal fun invoicePdfRecipient(
    recipientCompanyName: String,
    recipientCompanyAddress: String,
    regularFont: PdfFont,
    boldFont: PdfFont
): Table {
    val recipientTable = Table(1).apply {
        width = UnitValue.createPercentValue(100f)
    }

    recipientTable.addCell(
        recipientCell(
            recipientCompanyName = recipientCompanyName,
            recipientCompanyAddress = recipientCompanyAddress,
            regularFont = regularFont,
            boldFont = boldFont
        )
    )
    return recipientTable
}

private fun recipientCell(
    recipientCompanyName: String,
    recipientCompanyAddress: String,
    regularFont: PdfFont,
    boldFont: PdfFont
) =
    Cell()
        .add(
            Paragraph("INVOICE FOR")
                .setFont(boldFont)
                .setFontSize(PdfStyle.FontSize.Small)
                .setFontColor(PdfStyle.Color.Primary)
        )
        .add(Paragraph(recipientCompanyName).setFont(boldFont).setFontSize(PdfStyle.FontSize.Medium))
        .add(Paragraph(recipientCompanyAddress).setFont(regularFont).setFontSize(PdfStyle.FontSize.Small))
        .setBorder(null)

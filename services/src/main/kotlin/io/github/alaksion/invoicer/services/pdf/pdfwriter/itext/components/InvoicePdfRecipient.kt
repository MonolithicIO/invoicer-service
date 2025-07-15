package io.github.alaksion.invoicer.services.pdf.pdfwriter.itext.components

import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.UnitValue

@Suppress("MagicNumber")
internal fun invoicePdfRecipient(
    recipientCompanyName: String,
    boldFont: PdfFont
): Table {
    val recipientTable = Table(1).apply {
        width = UnitValue.createPercentValue(100f)
    }

    recipientTable.addCell(
        recipientCell(
            recipientCompanyName = recipientCompanyName,
            boldFont = boldFont
        )
    )
    return recipientTable
}

private fun recipientCell(
    recipientCompanyName: String,
    boldFont: PdfFont
) =
    Cell()
        .add(
            Paragraph("FOR")
                .setFont(boldFont)
                .setFontSize(PdfStyle.FontSize.Small)
                .setFontColor(PdfStyle.Color.Primary)
        )
        .add(Paragraph(recipientCompanyName).setFont(boldFont).setFontSize(PdfStyle.FontSize.Medium))

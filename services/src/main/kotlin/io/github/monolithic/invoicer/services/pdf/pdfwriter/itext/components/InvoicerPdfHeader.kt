package io.github.monolithic.invoicer.services.pdf.pdfwriter.itext.components

import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue

@Suppress("MagicNumber", "LongParameterList")
internal fun buildHeader(
    senderCompanyName: String,
    senderCompanyAddress: String,
    externalId: String,
    id: String,
    dueDate: String,
    issueDate: String,
    regularFont: PdfFont,
    boldFont: PdfFont
): Table {
    val table = Table(UnitValue.createPercentArray(floatArrayOf(50f, 50f)))
        .apply {
            width = UnitValue.createPercentValue(100f)
        }

    table.addCell(
        logoCell(
            senderCompanyName = senderCompanyName,
            senderCompanyAddress = senderCompanyAddress,
            regularFont = regularFont,
            boldFont = boldFont
        )
    )
    table.addCell(
        detailsCell(
            externalId = externalId,
            id = id,
            dueDate = dueDate,
            issueDate = issueDate,
            regularFont = regularFont,
            boldFont = boldFont
        )
    )
    return table
}

private fun logoCell(
    senderCompanyName: String,
    senderCompanyAddress: String,
    regularFont: PdfFont,
    boldFont: PdfFont
) = Cell()
    .add(
        Paragraph("INVOICE")
            .setFont(boldFont)
            .setFontSize(PdfStyle.FontSize.XLarge)
            .setFontColor(PdfStyle.Color.Primary)
    )
    .add(Paragraph("\n"))
    .add(
        Paragraph("FROM")
            .setFont(boldFont)
            .setFontSize(PdfStyle.FontSize.Small)
            .setFontColor(PdfStyle.Color.Primary)
    )
    .add(Paragraph(senderCompanyName).setFont(boldFont).setFontSize(PdfStyle.FontSize.Medium))
    .add(Paragraph(senderCompanyAddress).setFont(regularFont).setFontSize(PdfStyle.FontSize.Small))
    .setBorder(null)

@Suppress("LongParameterList")
private fun detailsCell(
    externalId: String,
    id: String,
    dueDate: String,
    issueDate: String,
    regularFont: PdfFont,
    boldFont: PdfFont
) = Cell()
    .add(
        Paragraph("Nº: $externalId").setFont(boldFont).setFontSize(PdfStyle.FontSize.Small)
            .setTextAlignment(TextAlignment.RIGHT)
    )
    .add(
        Paragraph("ID: $id").setFont(regularFont).setFontSize(PdfStyle.FontSize.XSmall)
            .setTextAlignment(TextAlignment.RIGHT)
    )
    .add(
        Paragraph("Issue Date: $issueDate").setFont(regularFont).setFontSize(PdfStyle.FontSize.Small)
            .setTextAlignment(TextAlignment.RIGHT)
    )
    .add(
        Paragraph("Due Date: $dueDate").setFont(regularFont).setFontSize(PdfStyle.FontSize.Small)
            .setTextAlignment(TextAlignment.RIGHT)
    )
    .setBorder(null)

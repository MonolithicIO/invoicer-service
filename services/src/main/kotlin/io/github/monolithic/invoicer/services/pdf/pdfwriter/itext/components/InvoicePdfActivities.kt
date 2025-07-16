package io.github.monolithic.invoicer.services.pdf.pdfwriter.itext.components

import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import io.github.monolithic.invoicer.services.pdf.pdfwriter.itext.components.PdfStyle.formatCurrency
import io.github.monolithic.invoicer.models.invoice.InvoiceModelActivityModel

@Suppress("MagicNumber")
internal fun invoicePdfActivities(
    activities: List<InvoiceModelActivityModel>,
    regularFont: PdfFont,
    boldFont: PdfFont
): Table {
    val table = Table(
        UnitValue.createPercentArray(
            floatArrayOf(40f, 20f, 20f, 20f)
        )
    ).apply {
        width = UnitValue.createPercentValue(100f)
    }

    table.addCell(descriptionColumnTitle(boldFont))
    table.addCell(unitPriceColumnTile(boldFont))
    table.addCell(quantityColumnTitle(boldFont))
    table.addCell(totalColumnTitle(boldFont))

    var totalAmount = 0L
    activities.forEach { item ->
        val itemTotal = item.unitPrice * item.quantity
        totalAmount += itemTotal

        table.addCell(
            Cell().add(Paragraph(item.name).setFont(regularFont))
        )
        table.addCell(
            Cell().add(Paragraph(formatCurrency(item.unitPrice)).setFont(regularFont))
                .setTextAlignment(TextAlignment.RIGHT)
        )

        table.addCell(
            Cell().add(Paragraph(item.quantity.toString()).setFont(regularFont))
                .setTextAlignment(TextAlignment.RIGHT)
        )

        table.addCell(
            Cell().add(Paragraph(formatCurrency(itemTotal)).setFont(regularFont))
                .setTextAlignment(TextAlignment.RIGHT)
        )
    }

    table.addCell(totalLabel(boldFont))
    table.addCell(totalValue(totalAmount, boldFont))
    return table
}

private fun descriptionColumnTitle(boldFont: PdfFont) = Cell()
    .add(
        Paragraph("Description")
            .setFont(boldFont)
    )
    .setBackgroundColor(PdfStyle.Color.Primary)
    .setFontColor(PdfStyle.Color.Background)

private fun unitPriceColumnTile(boldFont: PdfFont) = Cell()
    .add(
        Paragraph("Unite Price")
            .setFont(boldFont)
    )
    .setBackgroundColor(PdfStyle.Color.Primary)
    .setFontColor(PdfStyle.Color.Background)
    .setTextAlignment(TextAlignment.RIGHT)

private fun quantityColumnTitle(boldFont: PdfFont) = Cell()
    .add(
        Paragraph("Quantity")
            .setFont(boldFont)
    )
    .setBackgroundColor(PdfStyle.Color.Primary)
    .setFontColor(PdfStyle.Color.Background)
    .setTextAlignment(TextAlignment.RIGHT)

private fun totalColumnTitle(boldFont: PdfFont) = Cell()
    .add(
        Paragraph("Total")
            .setFont(boldFont)
    )
    .setBackgroundColor(PdfStyle.Color.Primary)
    .setFontColor(PdfStyle.Color.Background)
    .setTextAlignment(TextAlignment.RIGHT)

@Suppress("MagicNumber")
private fun totalLabel(boldFont: PdfFont) = Cell(1, 3)
    .add(
        Paragraph("TOTAL")
            .setFont(boldFont)
    )
    .setBorder(null)
    .setTextAlignment(TextAlignment.RIGHT)

private fun totalValue(
    value: Long,
    boldFont: PdfFont
) = Cell()
    .add(
        Paragraph(formatCurrency(value)).setFont(boldFont)
    )
    .setBackgroundColor(PdfStyle.Color.Overlay)
    .setTextAlignment(TextAlignment.RIGHT)

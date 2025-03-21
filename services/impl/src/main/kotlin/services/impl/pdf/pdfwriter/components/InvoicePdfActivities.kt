package services.impl.pdf.pdfwriter.components

import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import models.InvoiceModelActivityModel
import services.impl.pdf.pdfwriter.components.PdfStyle.formatCurrency

internal fun invoicePdfActivities(
    activities: List<InvoiceModelActivityModel>
): Table {
    val table = Table(
        UnitValue.createPercentArray(
            floatArrayOf(40f, 20f, 20f, 20f)
        )
    ).apply {
        width = UnitValue.createPercentValue(100f)
    }

    table.addCell(descriptionColumnTitle)
    table.addCell(unitPriceColumnTile)
    table.addCell(quantityColumnTitle)
    table.addCell(totalColumnTitle)

    var totalAmount = 0L
    activities.forEach { item ->
        val itemTotal = item.unitPrice * item.quantity
        totalAmount += itemTotal

        table.addCell(
            Cell().add(Paragraph(item.name).setFont(PdfStyle.Font.Regular))
        )
        table.addCell(
            Cell().add(Paragraph(formatCurrency(item.unitPrice)).setFont(PdfStyle.Font.Regular))
                .setTextAlignment(TextAlignment.RIGHT)
        )

        table.addCell(
            Cell().add(Paragraph(item.quantity.toString()).setFont(PdfStyle.Font.Regular))
                .setTextAlignment(TextAlignment.RIGHT)
        )

        table.addCell(
            Cell().add(Paragraph(formatCurrency(itemTotal)).setFont(PdfStyle.Font.Regular))
                .setTextAlignment(TextAlignment.RIGHT)
        )
    }

    table.addCell(totalLabel)
    table.addCell(totalValue(totalAmount))
    return table
}

private val descriptionColumnTitle = Cell()
    .add(
        Paragraph("Description")
            .setFont(PdfStyle.Font.Bold)
    )
    .setBackgroundColor(PdfStyle.Color.Primary)
    .setFontColor(PdfStyle.Color.Background)

private val unitPriceColumnTile = Cell()
    .add(
        Paragraph("Unite Price")
            .setFont(PdfStyle.Font.Bold)
    )
    .setBackgroundColor(PdfStyle.Color.Primary)
    .setFontColor(PdfStyle.Color.Background)
    .setTextAlignment(TextAlignment.RIGHT)

private val quantityColumnTitle = Cell()
    .add(
        Paragraph("Quantity")
            .setFont(PdfStyle.Font.Bold)
    )
    .setBackgroundColor(PdfStyle.Color.Primary)
    .setFontColor(PdfStyle.Color.Background)
    .setTextAlignment(TextAlignment.RIGHT)

private val totalColumnTitle = Cell()
    .add(
        Paragraph("Total")
            .setFont(PdfStyle.Font.Bold)
    )
    .setBackgroundColor(PdfStyle.Color.Primary)
    .setFontColor(PdfStyle.Color.Background)
    .setTextAlignment(TextAlignment.RIGHT)

private val totalLabel = Cell(1, 3)
    .add(
        Paragraph("TOTAL")
            .setFont(PdfStyle.Font.Bold)
    )
    .setBorder(null)
    .setTextAlignment(TextAlignment.RIGHT)

private fun totalValue(value: Long) = Cell()
    .add(
        Paragraph(formatCurrency(value)).setFont(PdfStyle.Font.Bold)
    )
    .setBackgroundColor(PdfStyle.Color.Overlay)
    .setTextAlignment(TextAlignment.RIGHT)
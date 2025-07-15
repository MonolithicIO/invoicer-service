package io.github.alaksion.invoicer.services.pdf.pdfwriter.itext.components

import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.layout.element.BlockElement
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import kotlinx.datetime.Instant
import io.github.alaksion.invoicer.services.pdf.pdfwriter.itext.components.PdfStyle.formatDate

internal fun invoicePdfFooter(
    createdAt: Instant,
    updatedAt: Instant,
    regularFont: PdfFont,
): Table {
    val footerTable = Table(1).apply {
        BlockElement.setWidth = UnitValue.createPercentValue(MAX_WIDTH_MULTIPLIER)
    }

    footerTable.addCell(
        Cell()
            .add(
                Paragraph("Created at: ${formatDate(createdAt)} - Last updated at: ${formatDate(updatedAt)}")
                    .setFont(regularFont)
                    .setFontSize(PdfStyle.FontSize.Small)
            )
            .setTextAlignment(TextAlignment.CENTER)
            .setBorder(null)
    )

    return footerTable
}

private const val MAX_WIDTH_MULTIPLIER = 100F

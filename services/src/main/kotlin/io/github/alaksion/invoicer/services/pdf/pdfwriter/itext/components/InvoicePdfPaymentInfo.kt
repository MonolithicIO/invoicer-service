package io.github.alaksion.invoicer.services.pdf.pdfwriter.itext.components

import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.BlockElement
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.UnitValue
import models.invoice.InvoicePayAccountModel

@Suppress("MagicNumber")
internal fun invoicePdfPaymentInfo(
    primary: InvoicePayAccountModel,
    intermediary: InvoicePayAccountModel?,
    regularFont: PdfFont,
    boldFont: PdfFont
): Table {
    val paymentTable = Table(1).apply {
        width = UnitValue.createPercentValue(100f)
    }

    paymentTable.addCell(
        paymentCell(
            primary = primary,
            intermediary = intermediary,
            regularFont = regularFont,
            boldFont = boldFont
        )
    )

    return paymentTable
}

private fun paymentCell(
    primary: InvoicePayAccountModel,
    intermediary: InvoicePayAccountModel?,
    regularFont: PdfFont,
    boldFont: PdfFont
): Cell {
    val cell = Cell()
        .add(
            Paragraph("PAYMENT INFORMATION")
                .setFont(boldFont)
                .setFontSize(PdfStyle.FontSize.Small)
                .setFontColor(PdfStyle.Color.Primary)
        )
        .setBorder(SolidBorder(PdfStyle.Color.Primary, 1f))
        .setPadding(PdfStyle.Spacing.Small)

    cell.add(Paragraph("\n"))

    cell.addPaymentInfo(
        label = "Pay Account",
        iban = primary.iban,
        swift = primary.swift,
        bankName = primary.bankName,
        bankAddress = primary.bankAddress,
        regularFont = regularFont,
        boldFont = boldFont
    )

    cell.add(Paragraph("\n"))
    cell.add(Paragraph("\n"))

    intermediary?.let {
        cell.addPaymentInfo(
            label = "Intermediary",
            iban = it.iban,
            swift = it.swift,
            bankName = it.bankName,
            bankAddress = it.bankAddress,
            regularFont = regularFont,
            boldFont = boldFont
        )
    }

    return cell
}

@Suppress("LongParameterList")
private fun Cell.addPaymentInfo(
    label: String,
    iban: String,
    swift: String,
    bankName: String,
    bankAddress: String,
    regularFont: PdfFont,
    boldFont: PdfFont
) {
    add(Paragraph("$label:").setFont(boldFont).setFontSize(PdfStyle.FontSize.Small))
    add(Paragraph("Iban: $iban").setFont(regularFont).setFontSize(PdfStyle.FontSize.Small))
    add(Paragraph("Swift: $swift").setFont(regularFont).setFontSize(PdfStyle.FontSize.Small))
    add(Paragraph("Bank Name: $bankName").setFont(regularFont).setFontSize(PdfStyle.FontSize.Small))
    add(Paragraph("Bank Address: $bankAddress").setFont(regularFont).setFontSize(PdfStyle.FontSize.Small))
}

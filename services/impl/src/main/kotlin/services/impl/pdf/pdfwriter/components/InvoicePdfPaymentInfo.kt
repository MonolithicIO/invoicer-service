package services.impl.pdf.pdfwriter.components

import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.UnitValue
import models.beneficiary.BeneficiaryModel
import models.intermediary.IntermediaryModel

internal fun invoicePdfPaymentInfo(
    beneficiary: BeneficiaryModel,
    intermediary: IntermediaryModel?,
    regularFont: PdfFont,
    boldFont: PdfFont
): Table {
    val paymentTable = Table(1).apply {
        width = UnitValue.createPercentValue(100f)
    }

    paymentTable.addCell(
        paymentCell(
            beneficiary = beneficiary,
            intermediary = intermediary,
            regularFont = regularFont,
            boldFont = boldFont
        )
    )

    return paymentTable
}

private fun paymentCell(
    beneficiary: BeneficiaryModel,
    intermediary: IntermediaryModel?,
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

    cell.addPaymentInfo(
        label = "Beneficiary",
        name = beneficiary.name,
        iban = beneficiary.iban,
        swift = beneficiary.swift,
        bankName = beneficiary.bankName,
        bankAddress = beneficiary.bankAddress,
        regularFont = regularFont,
        boldFont = boldFont
    )

    intermediary?.let {
        cell.addPaymentInfo(
            label = "Intermediary",
            name = it.name,
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

private fun Cell.addPaymentInfo(
    label: String,
    name: String,
    iban: String,
    swift: String,
    bankName: String,
    bankAddress: String,
    regularFont: PdfFont,
    boldFont: PdfFont
) {
    add(Paragraph("$label:").setFont(boldFont).setFontSize(PdfStyle.FontSize.Small))
    add(Paragraph("Name: $name").setFont(regularFont).setFontSize(PdfStyle.FontSize.Small))
    add(Paragraph("Iban: $iban").setFont(regularFont).setFontSize(PdfStyle.FontSize.Small))
    add(Paragraph("Swift: $swift").setFont(regularFont).setFontSize(PdfStyle.FontSize.Small))
    add(Paragraph("Bank Name: $bankName").setFont(regularFont).setFontSize(PdfStyle.FontSize.Small))
    add(Paragraph("Bank Address: $bankAddress").setFont(regularFont).setFontSize(PdfStyle.FontSize.Small))
}
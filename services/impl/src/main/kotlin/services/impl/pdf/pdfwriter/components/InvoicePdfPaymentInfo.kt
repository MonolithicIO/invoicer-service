package services.impl.pdf.pdfwriter.components

import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.UnitValue
import models.beneficiary.BeneficiaryModel
import models.intermediary.IntermediaryModel

internal fun invoicePdfPaymentInfo(
    beneficiary: BeneficiaryModel,
    intermediary: IntermediaryModel?
): Table {
    val paymentTable = Table(1).apply {
        width = UnitValue.createPercentValue(100f)
    }

    paymentTable.addCell(
        paymentCell(
            beneficiary = beneficiary,
            intermediary = intermediary
        )
    )

    return paymentTable
}

private fun paymentCell(
    beneficiary: BeneficiaryModel,
    intermediary: IntermediaryModel?
): Cell {
    val cell = Cell()
        .add(
            Paragraph("PAYMENT INFORMATION")
                .setFont(PdfStyle.Font.Bold)
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
        bankAddress = beneficiary.bankAddress
    )

    intermediary?.let {
        cell.addPaymentInfo(
            label = "Intermediary",
            name = it.name,
            iban = it.iban,
            swift = it.swift,
            bankName = it.bankName,
            bankAddress = it.bankAddress
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
    bankAddress: String
) {
    add(Paragraph("$label:").setFont(PdfStyle.Font.Bold).setFontSize(PdfStyle.FontSize.Small))
    add(Paragraph("Name: $name").setFont(PdfStyle.Font.Regular).setFontSize(PdfStyle.FontSize.Small))
    add(Paragraph("Iban: $iban").setFont(PdfStyle.Font.Regular).setFontSize(PdfStyle.FontSize.Small))
    add(Paragraph("Swift: $swift").setFont(PdfStyle.Font.Regular).setFontSize(PdfStyle.FontSize.Small))
    add(Paragraph("Bank Name: $bankName").setFont(PdfStyle.Font.Regular).setFontSize(PdfStyle.FontSize.Small))
    add(Paragraph("Bank Address: $bankAddress").setFont(PdfStyle.Font.Regular).setFontSize(PdfStyle.FontSize.Small))
}
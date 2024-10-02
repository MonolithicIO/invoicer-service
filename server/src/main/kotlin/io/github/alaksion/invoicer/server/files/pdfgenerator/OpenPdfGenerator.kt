package io.github.alaksion.invoicer.server.files.pdfgenerator

import com.lowagie.text.*
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import io.github.alaksion.invoicer.server.files.filehandler.FileHandler
import io.github.alaksion.invoicer.server.files.pdfgenerator.components.OpenPdfSpacer
import io.github.alaksion.invoicer.server.util.formatUsAmount
import models.InvoiceModel
import models.InvoiceModelActivityModel
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class OpenPdfGenerator(
    private val fileHandler: FileHandler
) : PdfGenerator {

    override suspend fun generate(
        invoice: InvoiceModel,
    ): FilePath {
        return suspendCoroutine { continuation ->
            val file = File(
                fileHandler.createFile(
                    path = PdfGenerator.tempPath,
                    fileName = "${invoice.id}.pdf"
                )
            )
            runCatching {
                val document = Document()
                PdfWriter.getInstance(document, FileOutputStream(file))
                document.open()
                document.add(
                    invoiceHeader(
                        recipientName = invoice.recipientCompanyName,
                        recipientAddress = invoice.recipientCompanyAddress,
                        senderAddress = invoice.senderCompanyAddress,
                        senderName = invoice.senderCompanyName,
                        id = invoice.externalId,
                        issueDate = invoice.issueDate.toString(),
                        dueDate = invoice.dueDate.toString()
                    )
                )
                document.add(OpenPdfSpacer(2))
                document.add(activitiesTable(activities = invoice.activities))
                document.add(OpenPdfSpacer(2))
                document.add(
                    amountDueTable(
                        invoice.activities
                            .map { it.amount }
                            .reduce { acc, l -> acc + l }
                    )
                )
                document.close()
            }.fold(
                onFailure = {
                    fileHandler.removeFile(file.absolutePath)
                    continuation.resumeWithException(it)
                },
                onSuccess = {
                    continuation.resume(file.absolutePath)
                }
            )
        }
    }

    private fun invoiceHeader(
        id: String,
        dueDate: String,
        issueDate: String,
        recipientName: String,
        recipientAddress: String,
        senderName: String,
        senderAddress: String
    ): PdfPTable {
        val mainTable = PdfPTable(2)
        mainTable.widthPercentage = 100f


        val rightInnerCell = PdfPTable(1)
        val rightCell = PdfPCell()
        rightCell.border = PdfPCell.NO_BORDER
        rightCell.verticalAlignment = Element.ALIGN_MIDDLE
        rightCell.addElement(
            headerTable(
                leadingText = "From",
                topText = senderName,
                bottomText = senderAddress
            )
        )
        val spacer = Paragraph(" ")
        spacer.alignment = Element.ALIGN_RIGHT
        rightCell.addElement(spacer)
        rightCell.addElement(
            headerTable(
                leadingText = "Invoice to",
                topText = recipientName,
                bottomText = recipientAddress
            )
        )
        rightInnerCell.addCell(rightCell)


        mainTable.addCell(
            leftTable(
                id = id,
                dueDate = dueDate,
                issueDate = issueDate
            )
        )
        mainTable.addCell(rightInnerCell)
        return mainTable
    }

    private fun headerTable(
        leadingText: String,
        topText: String,
        bottomText: String
    ): PdfPTable {
        val table = PdfPTable(2)

        val leadingCell = PdfPCell()
        leadingCell.verticalAlignment = Element.ALIGN_MIDDLE
        leadingCell.border = PdfPCell.NO_BORDER
        leadingCell.addElement(Paragraph(leadingText))

        val trailingCell = PdfPCell()
        trailingCell.border = PdfPCell.NO_BORDER
        trailingCell.verticalAlignment = Element.ALIGN_MIDDLE
        trailingCell.addElement(Paragraph(topText, Font(Font.HELVETICA, -1f, Font.BOLD)))
        trailingCell.addElement(Paragraph(bottomText, Font(Font.HELVETICA, -1f)))

        table.addCell(leadingCell)
        table.addCell(trailingCell)

        return table
    }

    private fun leftTable(
        id: String,
        dueDate: String,
        issueDate: String,
    ): PdfPTable {
        val table = PdfPTable(1)

        val leftCell = PdfPCell()
        leftCell.border = PdfPCell.NO_BORDER
        val idFont = Font(Font.HELVETICA, 24f, Font.BOLD)

        leftCell.addElement(Paragraph("INVOICE #${id}", idFont))
        leftCell.addElement(Paragraph("\n\n\n\n\n\n"))
        leftCell.addElement(Paragraph("Issue Date $issueDate"))
        leftCell.addElement(Paragraph("Due Date $dueDate"))
        table.addCell(leftCell)

        return table
    }

    private fun activitiesTable(
        activities: List<InvoiceModelActivityModel>
    ): PdfPTable {
        val table = PdfPTable(4)
        table.widthPercentage = 100f
        table.setWidths(floatArrayOf(50f, 10f, 20f, 20f))

        val descriptionCell = PdfPCell().apply {
            horizontalAlignment = Element.ALIGN_MIDDLE
            border = Rectangle.BOTTOM
            paddingBottom = 5f
            addElement(Paragraph("Service Description"))
        }

        val quantityCell = PdfPCell().apply {
            horizontalAlignment = Element.ALIGN_MIDDLE
            border = Rectangle.BOTTOM
            paddingBottom = 5f
            addElement(Paragraph("Quantity"))
        }

        val unitPriceCell = PdfPCell().apply {
            horizontalAlignment = Element.ALIGN_MIDDLE
            border = Rectangle.BOTTOM
            paddingBottom = 5f
            addElement(Paragraph("Unit Price"))
        }

        val amountCell = PdfPCell().apply {
            horizontalAlignment = Element.ALIGN_MIDDLE
            border = Rectangle.BOTTOM
            paddingBottom = 5f
            addElement(Paragraph("Amount"))
        }

        table.addCell(descriptionCell)
        table.addCell(quantityCell)
        table.addCell(unitPriceCell)
        table.addCell(amountCell)

        activities.forEach {
            table.addCell(Paragraph(it.name)).apply {
                horizontalAlignment = Element.ALIGN_MIDDLE
                border = PdfPCell.NO_BORDER
            }
            table.addCell(Paragraph(it.quantity.toString())).apply {
                horizontalAlignment = Element.ALIGN_MIDDLE
                border = PdfPCell.NO_BORDER
            }
            table.addCell(Paragraph(it.unitPrice.formatUsAmount())).apply {
                horizontalAlignment = Element.ALIGN_MIDDLE
                border = PdfPCell.NO_BORDER
            }
            table.addCell(Paragraph(it.amount.formatUsAmount())).apply {
                horizontalAlignment = Element.ALIGN_MIDDLE
                border = PdfPCell.NO_BORDER
            }
        }
        return table
    }

    private fun amountDueTable(
        value: Long
    ): PdfPTable {
        val table = PdfPTable(2)
        table.widthPercentage = 40f
        table.addCell(PdfPCell(Paragraph("Amount Due:"))).apply {
            border = PdfPCell.NO_BORDER
            horizontalAlignment = Element.ALIGN_RIGHT
        }
        table.addCell(PdfPCell(Paragraph(value.formatUsAmount()))).apply {
            border = PdfPCell.NO_BORDER
            horizontalAlignment = Element.ALIGN_RIGHT
        }
        table.horizontalAlignment = Element.ALIGN_RIGHT
        return table
    }

}
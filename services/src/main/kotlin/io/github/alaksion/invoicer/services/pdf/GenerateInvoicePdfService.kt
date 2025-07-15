package io.github.alaksion.invoicer.services.pdf

import io.github.alaksion.invoicer.foundation.storage.local.LocalStorage
import io.github.alaksion.invoicer.foundation.storage.remote.FileUploader
import java.util.*
import repository.InvoicePdfRepository
import services.api.services.invoice.GetUserInvoiceByIdService
import io.github.alaksion.invoicer.services.user.GetUserByIdService
import io.github.alaksion.invoicer.services.pdf.pdfwriter.InvoicePdfWriter

interface GenerateInvoicePdfService {
    suspend fun generate(
        invoiceId: UUID,
        userId: UUID
    )
}

@Suppress("UnusedPrivateProperty")
internal class GenerateInvoicePdfServiceImpl(
    private val getUserByIdService: GetUserByIdService,
    private val getUserInvoiceByIdService: GetUserInvoiceByIdService,
    private val writer: InvoicePdfWriter,
    private val fileUploader: FileUploader,
    private val invoicePdfRepository: InvoicePdfRepository,
    private val localStorage: LocalStorage
) : GenerateInvoicePdfService {

    override suspend fun generate(invoiceId: UUID, userId: UUID) {
//        getUserByIdService.get(userId)
//
//        val invoice = getUserInvoiceByIdService.get(
//            invoiceId = invoiceId
//        ) ?: throw IllegalArgumentException("Invoice not found")
//
//        localStorage.createDirectory("/temp/pdfs")
//
//        invoicePdfRepository.createInvoicePdf(invoiceId)
//
//        val outputPath = localStorage.getRootPath() + "/temp/pdfs" + "/invoice-${invoice.id}.pdf"
//
//        writer.write(
//            invoice = invoice,
//            outputPath = outputPath
//        )
//
//        runCatching {
//            fileUploader.uploadFile(
//                localFilePath = outputPath,
//                fileName = "user/$userId/$invoiceId.pdf"
//            )
//        }.fold(
//            onFailure = {
//                invoicePdfRepository.updateInvoicePdfState(
//                    invoiceId = invoiceId,
//                    status = InvoicePdfStatus.Failed,
//                    filePath = ""
//                )
//                throw it
//            },
//            onSuccess = { fileKey ->
//                invoicePdfRepository.updateInvoicePdfState(
//                    invoiceId = invoiceId,
//                    status = InvoicePdfStatus.Success,
//                    filePath = fileKey
//                )
//            }
//        )
//
//        localStorage.deleteFile(outputPath)
    }
}

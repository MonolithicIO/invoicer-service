package io.github.monolithic.invoicer.services.pdf

import io.github.monolithic.invoicer.foundation.storage.local.LocalStorage
import io.github.monolithic.invoicer.foundation.storage.remote.FileUploader
import io.github.monolithic.invoicer.models.invoicepdf.InvoicePdfStatus
import io.github.monolithic.invoicer.repository.InvoicePdfRepository
import io.github.monolithic.invoicer.services.invoice.GetUserInvoiceByIdService
import io.github.monolithic.invoicer.services.pdf.pdfwriter.InvoicePdfWriter
import io.github.monolithic.invoicer.services.user.GetUserByIdService
import java.util.*

interface GenerateInvoicePdfService {
    suspend fun generate(
        invoiceId: UUID,
        userId: UUID,
        companyId: UUID
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

    override suspend fun generate(invoiceId: UUID, userId: UUID, companyId: UUID) {
        getUserByIdService.get(userId)

        val invoice = getUserInvoiceByIdService.get(
            invoiceId = invoiceId,
            companyId = companyId,
            userId = userId
        )

        localStorage.createDirectory("/temp/pdfs")

        invoicePdfRepository.createInvoicePdf(invoiceId)

        val outputPath = localStorage.getRootPath() + "/temp/pdfs" + "/invoice-${invoice.id}.pdf"

        writer.write(
            invoice = invoice,
            outputPath = outputPath
        )

        runCatching {
            fileUploader.uploadFile(
                localFilePath = outputPath,
                fileName = "user/$userId/$invoiceId.pdf"
            )
        }.fold(
            onFailure = {
                invoicePdfRepository.updateInvoicePdfState(
                    invoiceId = invoiceId,
                    status = InvoicePdfStatus.Failed,
                    filePath = ""
                )
                throw it
            },
            onSuccess = { fileKey ->
                invoicePdfRepository.updateInvoicePdfState(
                    invoiceId = invoiceId,
                    status = InvoicePdfStatus.Success,
                    filePath = fileKey
                )
            }
        )

        localStorage.deleteFile(outputPath)
    }
}

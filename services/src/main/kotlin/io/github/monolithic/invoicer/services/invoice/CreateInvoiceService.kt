package io.github.monolithic.invoicer.services.invoice

import io.github.monolithic.invoicer.foundation.messaging.MessageProducer
import io.github.monolithic.invoicer.foundation.messaging.MessageTopic
import io.github.monolithic.invoicer.services.company.GetCompanyDetailsService
import io.github.monolithic.invoicer.services.customer.GetCustomerByIdService
import io.github.monolithic.invoicer.utils.date.toLocalDate
import io.github.monolithic.invoicer.utils.uuid.parseUuid
import io.github.monolithic.invoicer.models.createinvoice.CreateInvoiceResponseModel
import io.github.monolithic.invoicer.models.invoice.CreateInvoiceDTO
import java.util.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import io.github.monolithic.invoicer.models.invoice.CreateInvoiceActivityModel
import io.github.monolithic.invoicer.models.invoice.CreateInvoiceModel
import io.github.monolithic.invoicer.repository.InvoiceRepository
import io.github.monolithic.invoicer.services.user.GetUserByIdService
import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError
import io.github.monolithic.invoicer.foundation.exceptions.http.conflictError
import io.github.monolithic.invoicer.foundation.exceptions.http.forbiddenError
import io.github.monolithic.invoicer.foundation.exceptions.http.notFoundError

interface CreateInvoiceService {
    suspend fun createInvoice(
        model: CreateInvoiceDTO,
        userId: UUID
    ): CreateInvoiceResponseModel
}

internal class CreateInvoiceServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val clock: Clock,
    private val getUserByIdService: GetUserByIdService,
    private val messageProducer: MessageProducer,
    private val getCompanyDetailsService: GetCompanyDetailsService,
    private val getCustomerByIdService: GetCustomerByIdService
) : CreateInvoiceService {

    override suspend fun createInvoice(
        model: CreateInvoiceDTO,
        userId: UUID,
    ): CreateInvoiceResponseModel {
        validateActivities(model.activities)

        validateDateRange(
            issueDate = model.issueDate,
            dueDate = model.dueDate
        )

        val user = getUserByIdService.get(userId)

        val companyDetails = getCompanyDetailsService.get(model.companyId) ?: notFoundError("Company not found")

        if (user.id != companyDetails.user.id) forbiddenError()

        val customer = getCustomerByIdService.get(model.customerId) ?: notFoundError("Customer not found")

        if (customer.companyId != model.companyId) forbiddenError()

        if (invoiceRepository.getByInvoiceNumber(invoiceNumber = model.invoicerNumber) != null)
            conflictError("Invoice with externalId: ${model.invoicerNumber} already exists")


        val response =
            invoiceRepository.create(
                data = CreateInvoiceModel(
                    customer = customer,
                    company = companyDetails,
                    invoicerNumber = model.invoicerNumber,
                    activities = model.activities,
                    issueDate = model.issueDate,
                    dueDate = model.dueDate
                ),
                userId = userId
            )

        messageProducer.produceMessage(
            topic = MessageTopic.INVOICE_PDF,
            key = response,
            value = """
                {
                    "invoiceId": "$response",
                    "userId": "$userId",
                    "companyId": "${model.companyId}",
                    "type": "invoice_generate_pdf"
                }
            """.trimIndent()
        )

        return CreateInvoiceResponseModel(
            externalInvoiceId = model.invoicerNumber,
            invoiceId = parseUuid(response)
        )
    }

    private fun validateDateRange(
        issueDate: LocalDate,
        dueDate: LocalDate
    ) {
        val now = clock.now().toLocalDate()

        if (issueDate < now)
            badRequestError("Issue date cannot be past date")

        if (dueDate < now) {
            badRequestError("Due date cannot be past date")
        }

        if (issueDate > dueDate) {
            badRequestError("Issue date cannot be after due date")
        }
    }

    private fun validateActivities(
        services: List<CreateInvoiceActivityModel>
    ) {
        if (services.isEmpty())
            badRequestError("Invoice must have at least one service")

        services.forEach {
            if (it.quantity <= 0)
                badRequestError("Invoice activity must have quantity > 0")


            if (it.unitPrice <= 0) {
                badRequestError("Invoice activity must have unitPrice > 0")
            }
        }
    }

}

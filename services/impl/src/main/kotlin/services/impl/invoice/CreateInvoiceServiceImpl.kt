package services.impl.invoice

import io.github.alaksion.invoicer.foundation.messaging.MessageProducer
import io.github.alaksion.invoicer.foundation.messaging.MessageTopic
import io.github.alaksion.invoicer.utils.date.toLocalDate
import io.github.alaksion.invoicer.utils.uuid.parseUuid
import java.util.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import models.createinvoice.CreateInvoiceResponseModel
import models.invoice.CreateInvoiceActivityModel
import models.invoice.CreateInvoiceDTO
import models.invoice.CreateInvoiceModel
import repository.InvoiceRepository
import services.api.services.company.GetCompanyDetailsService
import services.api.services.customer.GetCustomerByIdService
import services.api.services.invoice.CreateInvoiceService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.badRequestError
import utils.exceptions.http.conflictError
import utils.exceptions.http.forbiddenError
import utils.exceptions.http.notFoundError


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
                    "type": "generate_pdf"
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

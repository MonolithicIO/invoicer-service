package services.impl.invoice

import io.github.alaksion.invoicer.foundation.messaging.MessageProducer
import io.github.alaksion.invoicer.foundation.messaging.MessageTopic
import io.github.alaksion.invoicer.utils.uuid.parseUuid
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import models.createinvoice.CreateInvoiceResponseModel
import models.invoice.CreateInvoiceActivityModel
import models.invoice.CreateInvoiceDTO
import models.invoice.CreateInvoiceModel
import repository.InvoiceRepository
import services.api.services.company.GetCompanyDetailsService
import services.api.services.customer.GetCustomerByIdService
import services.api.services.invoice.CreateInvoiceService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.*
import java.util.*


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

        if (invoiceRepository.getByInvoiceNumber(invoiceNumber = model.invoicerNumber) != null) {
            conflictError("Invoice with externalId: ${model.invoicerNumber} already exists")
        }

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
        issueDate: Instant,
        dueDate: Instant
    ) {
        if (clock.now() > issueDate) {
            httpError(
                message = "Issue date cannot be past date",
                code = HttpCode.BadRequest
            )
        }

        if (clock.now() > dueDate) {
            httpError(
                message = "Due date cannot be past date",
                code = HttpCode.BadRequest
            )
        }

        if (issueDate >= dueDate) {
            throw HttpError(
                message = "Issue date cannot be after due date",
                statusCode = HttpCode.BadRequest
            )
        }
    }

    private fun validateActivities(
        services: List<CreateInvoiceActivityModel>
    ) {
        if (services.isEmpty()) throw HttpError(
            message = "Invoice must have at least one service",
            statusCode = HttpCode.BadRequest
        )

        services.forEach {
            if (it.quantity <= 0) {
                throw HttpError(
                    message = "Invoice activity must have quantity > 0",
                    statusCode = HttpCode.BadRequest
                )
            }

            if (it.unitPrice <= 0) {
                throw HttpError(
                    message = "Invoice activity must have unitPrice > 0",
                    statusCode = HttpCode.BadRequest
                )
            }
        }
    }

}
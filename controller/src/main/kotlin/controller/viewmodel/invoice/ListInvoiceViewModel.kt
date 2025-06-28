package controller.viewmodel.invoice

import io.github.alaksion.invoicer.utils.uuid.parseUuid
import io.ktor.http.*
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import models.invoice.GetInvoicesFilterModel
import models.invoice.InvoiceListModel
import utils.exceptions.http.badRequestError

@Serializable
internal data class InvoiceListViewModel(
    val items: List<InvoiceListItemViewModel>,
    val totalItemCount: Long,
    val nextPage: Long?
)

@Serializable
internal data class InvoiceListItemViewModel(
    val id: String,
    val externalId: String,
    val issueDate: LocalDate,
    val dueDate: LocalDate,
    val createdAt: Instant,
    val updatedAt: Instant,
    val totalAmount: Long,
    val companyName: String,
    val customerName: String
)

internal fun InvoiceListModel.toViewModel(): InvoiceListViewModel {
    return InvoiceListViewModel(
        items = items.map {
            InvoiceListItemViewModel(
                id = it.id.toString(),
                externalId = it.invoiceNumber,
                companyName = it.companyName,
                customerName = it.customerName,
                issueDate = it.issueDate,
                dueDate = it.dueDate,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                totalAmount = it.totalAmount,
            )
        },
        totalItemCount = totalCount,
        nextPage = nextPage
    )
}

internal fun getInvoiceFilters(params: Parameters): GetInvoicesFilterModel {
    return GetInvoicesFilterModel(
        minIssueDate = parseDate(params["minIssueDate"], "Invalid date format: minIssueDate"),
        maxIssueDate = parseDate(params["maxIssueDate"], "Invalid date format: maxIssueDate"),
        minDueDate = parseDate(params["minDueDate"], "Invalid date format: minDueDate"),
        maxDueDate = parseDate(params["maxDueDate"], "Invalid date format: maxDueDate"),
        customerId = params["customerId"]?.let { parseUuid(it) }
    )
}

private fun parseDate(rawString: String?, errorMessage: String): Instant? {
    return rawString?.let {
        runCatching {
            Instant.parse(rawString)
        }.fold(
            onSuccess = {
                it
            },
            onFailure = {
                badRequestError(errorMessage)
            }
        )
    }
}
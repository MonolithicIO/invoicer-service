package controller.viewmodel.customer

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import models.customer.CustomerList

@Serializable
internal data class ListCostumerViewModel(
    val itemCount: Long,
    val nextPage: Long? = null,
    val items: List<CustomerListItemViewModel>
)

@Serializable
internal data class CustomerListItemViewModel(
    val id: String,
    val name: String,
    val email: String,
    val phone: String? = null,
    val companyId: String,
    val createdAt: Instant,
    val updatedAt: Instant
)

internal fun CustomerList.toViewModel(): ListCostumerViewModel {
    return ListCostumerViewModel(
        itemCount = itemCount,
        nextPage = nextPage,
        items = items.map {
            CustomerListItemViewModel(
                id = it.id,
                name = it.name,
                email = it.email,
                phone = it.phone,
                companyId = it.companyId,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    )
}
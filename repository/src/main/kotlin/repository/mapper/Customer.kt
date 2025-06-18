package repository.mapper

import models.customer.CustomerListItem
import repository.entities.CustomerEntity

internal fun CustomerEntity.toListItem(): CustomerListItem {
    return CustomerListItem(
        id = this.id.value.toString(),
        name = this.name,
        email = this.email,
        phone = this.phone,
        companyId = this.company.id.value.toString(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
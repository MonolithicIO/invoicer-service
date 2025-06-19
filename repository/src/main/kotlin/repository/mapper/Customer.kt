package repository.mapper

import models.customer.CustomerListItem
import models.customer.CustomerModel
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

internal fun CustomerEntity.toModel(): CustomerModel {
    return CustomerModel(
        id = this.id.value.toString(),
        name = this.name,
        email = this.email,
        phone = this.phone,
        companyId = this.company.id.value.toString(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
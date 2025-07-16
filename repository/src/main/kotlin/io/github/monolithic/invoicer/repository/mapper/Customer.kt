package io.github.monolithic.invoicer.repository.mapper

import io.github.monolithic.invoicer.repository.entities.CustomerEntity
import io.github.monolithic.invoicer.models.customer.CustomerListItem
import io.github.monolithic.invoicer.models.customer.CustomerModel

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
        id = this.id.value,
        name = this.name,
        email = this.email,
        phone = this.phone,
        companyId = this.company.id.value,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

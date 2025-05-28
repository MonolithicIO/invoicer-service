package datasource.impl.entities

import models.paymentaccount.PaymentAccountModel
import models.paymentaccount.PaymentAccountTypeModel
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

internal object PaymentAccountTable : UUIDTable("t_company_pay_account") {
    val iban = varchar("iban", 1000)
    val swift = varchar("swift", 11)
    val bankName = varchar("bank_name", 1000)
    val bankAddress = varchar("bank_address", 1000)
    val company = reference("company_id", foreign = UserCompanyTable, onDelete = ReferenceOption.CASCADE)
    val type = varchar("type", 50) // e.g., "primary" or "intermediary"
    val isDeleted = bool("is_deleted").default(false)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}

internal class PaymentAccountEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PaymentAccountEntity>(PaymentAccountTable)

    var iban by PaymentAccountTable.iban
    var swift by PaymentAccountTable.swift
    var bankName by PaymentAccountTable.bankName
    var bankAddress by PaymentAccountTable.bankAddress
    var type by PaymentAccountTable.type

    var company by UserCompanyEntity referencedOn PaymentAccountTable.company

    var isDeleted by PaymentAccountTable.isDeleted
    var createdAt by PaymentAccountTable.createdAt
    var updatedAt by PaymentAccountTable.updatedAt
}

internal fun PaymentAccountEntity.toModel() = PaymentAccountModel(
    iban = iban,
    swift = swift,
    bankName = bankName,
    bankAddress = bankAddress,
    type = when(type) {
        "primary" -> PaymentAccountTypeModel.Primary
        "intermediary" -> PaymentAccountTypeModel.Intermediary
        else -> throw IllegalArgumentException("Unknown payment account type: $type")
    },
    isDeleted = isDeleted,
    createdAt = createdAt,
    updatedAt = updatedAt
)
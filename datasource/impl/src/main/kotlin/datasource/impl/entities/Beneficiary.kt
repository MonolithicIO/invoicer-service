package datasource.impl.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

internal object BeneficiaryTable : UUIDTable("t_beneficiary") {
    val name = varchar("name", 1000)
    val iban = varchar("iban", 1000)
    val swift = varchar("swift", 11)
    val bankName = varchar("bank_name", 1000)
    val bankAddress = varchar("bank_address", 1000)
    val isDeleted = bool("is_deleted").default(false)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    val user = reference("user_id", foreign = UserTable, onDelete = ReferenceOption.CASCADE)
}

internal class BeneficiaryEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<BeneficiaryEntity>(BeneficiaryTable)

    var name by BeneficiaryTable.name
    var iban by BeneficiaryTable.iban
    var swift by BeneficiaryTable.swift
    var bankName by BeneficiaryTable.bankName
    var bankAddress by BeneficiaryTable.bankAddress
    var isDeleted by BeneficiaryTable.isDeleted
    var updatedAt by BeneficiaryTable.updatedAt
    val createdAt by BeneficiaryTable.createdAt
    val user by UserEntity referencedOn BeneficiaryTable.user
}
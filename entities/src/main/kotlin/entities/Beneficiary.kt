package entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.UUID

object BeneficiaryTable : UUIDTable("t_beneficiary") {
    val name = varchar("name", 1000)
    val iban = varchar("iban", 1000)
    val swift = varchar("swift", 11)
    val bankName = varchar("bank_name", 1000)
    val bankAddress = varchar("bank_address", 1000)
    val isDeleted = bool("is_deleted").default(false)
    val user = reference("user_id", foreign = UserTable, onDelete = ReferenceOption.CASCADE)
}

class BeneficiaryEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<BeneficiaryEntity>(BeneficiaryTable)

    var name by BeneficiaryTable.name
    var iban by BeneficiaryTable.iban
    var swift by BeneficiaryTable.swift
    var bankName by BeneficiaryTable.bankName
    var bankAddress by BeneficiaryTable.bankAddress
    var isDeleted by BeneficiaryTable.isDeleted
    val user by UserEntity referencedOn BeneficiaryTable.user
}

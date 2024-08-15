package io.github.alaksion.invoicer.server.data.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.UUID

internal object IntermediaryTable : UUIDTable("t_beneficiary") {
    val name = varchar("name", 1000)
    val iban = varchar("iban", 1000)
    val swift = varchar("swift", 11)
    val bankName = varchar("bank_name", 1000)
    val bankAddress = varchar("bank_address", 1000)
    val user = reference("user_id", foreign = UserTable, onDelete = ReferenceOption.CASCADE)
}

internal class IntermediaryEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<BeneficiaryEntity>(IntermediaryTable)

    var name by BeneficiaryTable.name
    var iban by BeneficiaryTable.iban
    var swift by BeneficiaryTable.swift
    var bankName by BeneficiaryTable.bankName
    var bankAddress by BeneficiaryTable.bankAddress
    val user by UserEntity referencedOn IntermediaryTable.user
}
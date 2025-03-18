package datasource.impl.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object IntermediaryTable : UUIDTable("t_intermediary") {
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

class IntermediaryEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<IntermediaryEntity>(IntermediaryTable)

    var name by IntermediaryTable.name
    var iban by IntermediaryTable.iban
    var swift by IntermediaryTable.swift
    var bankName by IntermediaryTable.bankName
    var bankAddress by IntermediaryTable.bankAddress
    var isDeleted by IntermediaryTable.isDeleted
    var updatedAt by IntermediaryTable.updatedAt
    val createdAt by IntermediaryTable.createdAt
    val user by UserEntity referencedOn IntermediaryTable.user
}
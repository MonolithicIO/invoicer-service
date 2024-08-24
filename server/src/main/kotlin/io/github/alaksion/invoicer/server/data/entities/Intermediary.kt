package io.github.alaksion.invoicer.server.data.entities

import io.github.alaksion.invoicer.server.domain.model.intermediary.IntermediaryModel
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.UUID

internal object IntermediaryTable : UUIDTable("t_intermediary") {
    val name = varchar("name", 1000)
    val iban = varchar("iban", 1000)
    val swift = varchar("swift", 11)
    val bankName = varchar("bank_name", 1000)
    val bankAddress = varchar("bank_address", 1000)
    val user = reference("user_id", foreign = UserTable, onDelete = ReferenceOption.CASCADE)
}

internal class IntermediaryEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<IntermediaryEntity>(IntermediaryTable)

    var name by IntermediaryTable.name
    var iban by IntermediaryTable.iban
    var swift by IntermediaryTable.swift
    var bankName by IntermediaryTable.bankName
    var bankAddress by IntermediaryTable.bankAddress
    val user by UserEntity referencedOn IntermediaryTable.user
}

internal fun IntermediaryEntity.toModel(): IntermediaryModel = IntermediaryModel(
    name = this.name,
    iban = this.iban,
    swift = this.swift,
    bankName = this.bankName,
    bankAddress = this.bankAddress,
    userId = this.user.id.value.toString(),
    id = this.id.value.toString()
)
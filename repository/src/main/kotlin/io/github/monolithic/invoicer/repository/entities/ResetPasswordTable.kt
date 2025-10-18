package io.github.monolithic.invoicer.repository.entities

import java.util.*
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

internal object ResetPasswordTable : UUIDTable("t_reset_password") {
    val safeCode = varchar("safe_code", 50)
    val user = reference(name = "user_id", foreign = UserTable, onDelete = ReferenceOption.CASCADE)
    val isConsumed = bool("is_consumed")
    val expirationText = varchar("expiration_text", 255)
    val expiresAt = timestamp("expires_at")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    val attempts = integer("attempts").default(0)
}

internal class ResetPasswordEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ResetPasswordEntity>(ResetPasswordTable)

    var safeCode by ResetPasswordTable.safeCode
    var user by UserEntity.Companion referencedOn ResetPasswordTable.user
    var isConsumed by ResetPasswordTable.isConsumed
    var expirationText by ResetPasswordTable.expirationText
    var expiresAt: kotlinx.datetime.Instant by ResetPasswordTable.expiresAt
    var createdAt: kotlinx.datetime.Instant by ResetPasswordTable.createdAt
    var updatedAt: kotlinx.datetime.Instant by ResetPasswordTable.updatedAt
    var attempts by ResetPasswordTable.attempts
}

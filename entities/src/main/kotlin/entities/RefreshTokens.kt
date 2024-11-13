package entities

import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.date
import java.util.*

object RefreshTokensTable : UUIDTable("t_refresh_tokens") {
    val user = reference(name = "user_id", foreign = UserTable, onDelete = ReferenceOption.CASCADE)
    val enabled = bool("enabled").default(true)
    val refreshToken = varchar("token", 1000)
    val createdAt = date("created_at")
    val updatedAt = date("updated_at")
}

class RefreshTokenEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RefreshTokenEntity>(RefreshTokensTable)

    var user by UserEntity referencedOn RefreshTokensTable.user
    var enabled by RefreshTokensTable.enabled
    var refreshToken by RefreshTokensTable.refreshToken
    var createdAt: LocalDate by InvoiceActivityTable.createdAt
    var updatedAt: LocalDate by InvoiceActivityTable.updatedAt
}
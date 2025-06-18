package repository.entities

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

internal object RefreshTokensTable : UUIDTable("t_refresh_tokens") {
    val user = reference(name = "user_id", foreign = UserTable, onDelete = ReferenceOption.CASCADE)
    val enabled = bool("enabled").default(true)
    val refreshToken = varchar("token", 1000)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}

internal class RefreshTokenEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RefreshTokenEntity>(RefreshTokensTable)

    var user by UserEntity.Companion referencedOn RefreshTokensTable.user
    var enabled by RefreshTokensTable.enabled
    var refreshToken by RefreshTokensTable.refreshToken
    var createdAt: Instant by RefreshTokensTable.createdAt
    var updatedAt: Instant by RefreshTokensTable.updatedAt
}
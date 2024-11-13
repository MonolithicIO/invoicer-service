package entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.*

object RefreshTokensTable : UUIDTable("t_refresh_tokens") {
    val user = reference(name = "user_id", foreign = UserTable, onDelete = ReferenceOption.CASCADE)
    val enabled = bool("enabled").default(true)
}

class RefreshTokenEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RefreshTokenEntity>(RefreshTokensTable)

    var user by UserEntity referencedOn RefreshTokensTable.user
    var enabled by RefreshTokensTable.enabled
}
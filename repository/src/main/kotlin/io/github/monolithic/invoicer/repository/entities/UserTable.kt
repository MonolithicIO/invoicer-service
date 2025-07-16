package io.github.monolithic.invoicer.repository.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

internal object UserTable : UUIDTable("T_USER") {
    val email = varchar(name = "email", length = 500)
    val password = varchar(name = "password", length = 60)
    val identityProviderUuid = varchar("identity_provider_uuid", 200)
        .nullable()
        .default(null)
    val verified = bool("verified")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}

internal class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UserTable)

    var email by UserTable.email
    var password by UserTable.password
    var verified by UserTable.verified
    val identityProviderUuid by UserTable.identityProviderUuid
    var updatedAt by UserTable.updatedAt
    val createdAt by UserTable.createdAt
}

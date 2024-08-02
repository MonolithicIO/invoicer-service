package io.github.alaksion.invoicer.server.data.entities

import io.github.alaksion.invoicer.server.domain.model.user.UserModel
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

internal object UserTable : UUIDTable("T_USER") {
    val email = varchar(name = "email", length = 500)
    val password = varchar(name = "password", length = 20)
    val verified = bool("verified")
}

internal class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UserTable)

    var email by UserTable.email
    var password by UserTable.password
    var verified by UserTable.verified

}

internal fun UserEntity.toModel(): UserModel = UserModel(
    id = this.id.value,
    password = this.password,
    verified = this.verified,
    email = this.email
)
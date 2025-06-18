package repository.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

internal object UserCompanyTable : UUIDTable("t_user_company") {
    val name = varchar("name", 1000)
    val document = varchar("registration_document", 1000)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    val isDeleted = bool("is_deleted")
    val user = reference("user_id", foreign = UserTable)
}

internal class UserCompanyEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserCompanyEntity>(UserCompanyTable)

    var name by UserCompanyTable.name
    var document by UserCompanyTable.document
    var createdAt by UserCompanyTable.createdAt
    var updatedAt by UserCompanyTable.updatedAt
    var isDeleted by UserCompanyTable.isDeleted
}
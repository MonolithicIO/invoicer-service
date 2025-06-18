package datasource.impl.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

internal object CustomerTable : UUIDTable("t_customer") {
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val phone = varchar("phone", 30).nullable()
    val company = reference("company_id", UserCompanyTable)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    val isDeleted = bool("is_deleted")
}

internal class CustomerEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CustomerEntity>(CustomerTable)

    var name by CustomerTable.name
    var email by CustomerTable.email
    var phone by CustomerTable.phone
    var company by UserCompanyEntity referencedOn CompanyAddressTable.company
    var createdAt by CustomerTable.createdAt
    var updatedAt by CustomerTable.updatedAt
    var isDeleted by CustomerTable.isDeleted
}
package repository.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

internal object CompanyContactTable : UUIDTable("t_company_contact") {
    val company = reference("company_id", UserCompanyTable)
    val contactEmail = varchar("contact_email", 1000).nullable()
    val contactPhone = varchar("contact_phone", 30).nullable()
    val isDeleted = bool("is_deleted")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}

internal class CompanyContactEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CompanyContactEntity>(CompanyContactTable)

    var contactEmail by CompanyContactTable.contactEmail
    var contactPhone by CompanyContactTable.contactPhone
    var isDeleted by CompanyContactTable.isDeleted
    var createdAt by CompanyContactTable.createdAt
    var updatedAt by CompanyContactTable.updatedAt

    var company by UserCompanyEntity.Companion referencedOn CompanyContactTable.company
}
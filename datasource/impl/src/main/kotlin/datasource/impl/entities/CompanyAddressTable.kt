package datasource.impl.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

internal object CompanyAddressTable : UUIDTable("t_company_address") {
    val company = reference("company_id", UserCompanyTable)
    val addressLine1 = varchar("address_line_1", 1000)
    val addressLine2 = varchar("address_line_2", 1000).nullable()
    val city = varchar("city", 500)
    val state = varchar("state", 500)
    val postalCode = varchar("postal_code", 20)
    // ISO 3166-1 alpha-3 country code
    val countryCode = varchar("country_code", 3)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    val isDeleted = bool("is_deleted")
}

internal class CompanyAddressEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CompanyAddressEntity>(CompanyAddressTable)

    var company by UserCompanyEntity referencedOn CompanyAddressTable.company

    var addressLine1 by CompanyAddressTable.addressLine1
    var addressLine2 by CompanyAddressTable.addressLine2
    var city by CompanyAddressTable.city
    var state by CompanyAddressTable.state
    var postalCode by CompanyAddressTable.postalCode
    var countryCode by CompanyAddressTable.countryCode
    var createdAt by CompanyAddressTable.createdAt
    var updatedAt by CompanyAddressTable.updatedAt
    var isDeleted by CompanyAddressTable.isDeleted
}
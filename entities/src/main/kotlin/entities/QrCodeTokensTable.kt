package entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

object QrCodeTokensTable : UUIDTable("") {
    val ipAddress = varchar("ip_address", 100)
    val agent = varchar("agent", 100)
    val base64Content = varchar("base64_content", 1000)
    val status = varchar("status", 100)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    val expiresAt = timestamp("expires_at")
}

class QrCodeEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<QrCodeEntity>(QrCodeTokensTable)

    var ipAddress by QrCodeTokensTable.ipAddress
    var agent by QrCodeTokensTable.agent
    var base64Content by QrCodeTokensTable.base64Content
    var status by QrCodeTokensTable.status
    val createdAt by QrCodeTokensTable.createdAt
    val expiresAt by QrCodeTokensTable.expiresAt
    var updatedAt by QrCodeTokensTable.updatedAt
}
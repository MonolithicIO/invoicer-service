package datasource.impl.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

internal object QrCodeTokensTable : UUIDTable("t_qrcode_tokens") {
    val ipAddress = varchar("ip_address", 100)
    val agent = varchar("agent", 1000)
    val content = varchar("content", 1000)
    // Apparently 1000 is not enough lol
    val base64Content = varchar("base64_content", 2000)
    val status = varchar("status", 100)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    val expiresAt = timestamp("expires_at")
}

internal class QrCodeTokenEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<QrCodeTokenEntity>(QrCodeTokensTable)

    var ipAddress by QrCodeTokensTable.ipAddress
    var agent by QrCodeTokensTable.agent
    var content by QrCodeTokensTable.content
    var base64Content by QrCodeTokensTable.base64Content
    var status by QrCodeTokensTable.status
    val createdAt by QrCodeTokensTable.createdAt
    val expiresAt by QrCodeTokensTable.expiresAt
    var updatedAt by QrCodeTokensTable.updatedAt
}
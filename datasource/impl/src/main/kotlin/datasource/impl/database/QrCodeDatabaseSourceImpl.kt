package datasource.impl.database

import datasource.api.database.QrCodeTokenDatabaseSource
import datasource.impl.mapper.toModel
import entities.QrCodeTokenEntity
import entities.QrCodeTokensTable
import models.qrcodetoken.QrCodeTokenModel
import org.jetbrains.exposed.sql.insertReturning
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.updateReturning
import utils.date.impl.DateProvider
import java.util.*
import kotlin.time.Duration.Companion.seconds

internal enum class QrCodeTokenStatus(
    val value: String
) {
    GENERATED("generated"),
    CONSUMED("consumed"),
    EXPIRED("expired")
}

internal class QrCodeDatabaseSourceImpl(
    private val dateProvider: DateProvider
) : QrCodeTokenDatabaseSource {

    override suspend fun createQrCodeToken(
        ipAddress: String,
        agent: String,
        base64Content: String,
        content: String,
    ): QrCodeTokenModel {
        return newSuspendedTransaction {
            QrCodeTokensTable.insertReturning {
                it[QrCodeTokensTable.ipAddress] = ipAddress
                it[QrCodeTokensTable.agent] = agent
                it[QrCodeTokensTable.base64Content] = base64Content
                it[QrCodeTokensTable.content] = content
                it[status] = QrCodeTokenStatus.GENERATED.value
                it[createdAt] = dateProvider.currentInstant()
                it[updatedAt] = dateProvider.currentInstant()
                it[expiresAt] = dateProvider.currentInstant().plus(60.seconds)
            }
        }.map {
            QrCodeTokenEntity.wrapRow(it)
        }.first().toModel()
    }

    override suspend fun getQrCodeTokenByContentId(contentId: String): QrCodeTokenModel? {
        return newSuspendedTransaction {
            QrCodeTokenEntity.find { QrCodeTokensTable.content eq contentId }
                .firstOrNull()
                ?.toModel()
        }
    }

    override suspend fun getQrCodeTokenByUUID(id: String): QrCodeTokenModel? {
        return newSuspendedTransaction {
            QrCodeTokenEntity.find { QrCodeTokensTable.id eq UUID.fromString(id) }
                .firstOrNull()
                ?.toModel()
        }
    }

    override suspend fun consumeQrCodeToken(id: String): QrCodeTokenModel {
        return newSuspendedTransaction {
            QrCodeTokensTable.updateReturning(
                where = {
                    QrCodeTokensTable.id eq UUID.fromString(id)
                }
            ) {
                it[status] = QrCodeTokenStatus.CONSUMED.value
                it[updatedAt] = dateProvider.currentInstant()
            }
        }.map {
            QrCodeTokenEntity.wrapRow(it).toModel()
        }.first()
    }

    override suspend fun expireQrCodeToken(id: String) {
        return newSuspendedTransaction {
            QrCodeTokensTable.updateReturning(
                where = {
                    QrCodeTokensTable.id eq UUID.fromString(id)
                }
            ) {
                it[status] = QrCodeTokenStatus.EXPIRED.value
                it[updatedAt] = dateProvider.currentInstant()
            }
        }
    }
}
package io.github.alaksion.invoicer.server.data.repository

import io.github.alaksion.invoicer.server.data.datasource.InvoiceDataSource
import io.github.alaksion.invoicer.server.data.entities.toListItemModel
import io.github.alaksion.invoicer.server.data.entities.toModel
import io.github.alaksion.invoicer.server.domain.model.CreateInvoiceModel
import io.github.alaksion.invoicer.server.domain.model.InvoiceModel
import io.github.alaksion.invoicer.server.domain.model.getinvoices.GetInvoicesFilterModel
import io.github.alaksion.invoicer.server.domain.model.getinvoices.InvoiceListItemModel
import io.github.alaksion.invoicer.server.domain.repository.InvoiceRepository
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*


internal class InvoiceRepositoryImpl(
    private val dataSource: InvoiceDataSource
) : InvoiceRepository {

    override suspend fun createInvoice(
        data: CreateInvoiceModel,
        userId: UUID,
    ): String {
        return newSuspendedTransaction {
            dataSource.createInvoice(data, userId)
        }
    }

    override suspend fun getInvoiceByUUID(id: UUID): InvoiceModel? {
        return newSuspendedTransaction {
            dataSource.getInvoiceByUUID(uuid = id)?.toModel()
        }
    }

    override suspend fun getInvoiceByExternalId(externalId: String): InvoiceModel? {
        return newSuspendedTransaction {
            dataSource.getInvoiceByExternalId(externalId = externalId)?.toModel()
        }
    }

    override suspend fun getInvoices(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        userId: String,
    ): List<InvoiceListItemModel> {
        return newSuspendedTransaction {
            dataSource.getInvoicesFiltered(
                page = page,
                limit = limit,
                filters = filters,
                userId = userId
            ).map {
                it.toListItemModel()
            }
        }
    }

    override suspend fun deleteByUUID(id: UUID) {
        newSuspendedTransaction {
            dataSource.deleteInvoice(id)
        }
    }

    override suspend fun getInvoicesByBeneficiaryId(
        beneficiaryId: UUID,
        userId: UUID
    ): List<InvoiceListItemModel> {
        return newSuspendedTransaction {
            dataSource.getInvoicesByBeneficiaryId(beneficiaryId, userId).map {
                it.toListItemModel()
            }
        }
    }

    override suspend fun getInvoicesByIntermediaryId(
        intermediaryId: UUID,
        userId: UUID
    ): List<InvoiceListItemModel> {
        return newSuspendedTransaction {
            dataSource.getInvoicesByIntermediaryId(intermediaryId, userId).map {
                it.toListItemModel()
            }
        }
    }


}
package repository

import datasource.api.database.InvoiceDatabaseSource
import datasource.api.model.invoice.CreateInvoiceActivityData
import datasource.api.model.invoice.CreateInvoiceData
import datasource.api.model.invoice.GetInvoicesFilterData
import foundation.cache.CacheHandler
import models.InvoiceModel
import models.createinvoice.CreateInvoiceModel
import models.getinvoices.GetInvoicesFilterModel
import models.getinvoices.InvoiceListItemModel
import models.getinvoices.InvoiceListModel
import java.util.*

interface InvoiceRepository {
    suspend fun createInvoice(
        data: CreateInvoiceModel,
        userId: UUID,
    ): String

    suspend fun getInvoiceByUUID(
        id: UUID
    ): InvoiceModel?

    suspend fun getInvoiceByExternalId(
        externalId: String
    ): InvoiceModel?

    suspend fun getInvoices(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        userId: UUID,
    ): InvoiceListModel

    suspend fun delete(
        id: UUID
    )

    suspend fun getInvoicesByBeneficiaryId(
        beneficiaryId: UUID,
        userId: UUID,
    ): List<InvoiceListItemModel>

    suspend fun getInvoicesByIntermediaryId(
        intermediaryId: UUID,
        userId: UUID,
    ): List<InvoiceListItemModel>
}


internal class InvoiceRepositoryImpl(
    private val databaseSource: InvoiceDatabaseSource,
    private val cacheHandler: CacheHandler
) : InvoiceRepository {

    override suspend fun createInvoice(
        data: CreateInvoiceModel,
        userId: UUID,
    ): String {
        return databaseSource.createInvoice(
            data = CreateInvoiceData(
                externalId = data.externalId,
                senderCompanyName = data.senderCompanyName,
                senderCompanyAddress = data.senderCompanyAddress,
                recipientCompanyName = data.recipientCompanyName,
                recipientCompanyAddress = data.recipientCompanyAddress,
                issueDate = data.issueDate,
                dueDate = data.dueDate,
                beneficiaryId = data.beneficiaryId,
                intermediaryId = data.intermediaryId,
                activities = data.activities.map {
                    CreateInvoiceActivityData(
                        description = it.description,
                        quantity = it.quantity,
                        unitPrice = it.unitPrice
                    )
                }
            ),
            userId = userId
        )
    }

    override suspend fun getInvoiceByUUID(id: UUID): InvoiceModel? {
        val cached = cacheHandler.get(
            key = id.toString(),
            serializer = InvoiceModel.serializer()
        )

        if (cached != null) return cached

        return databaseSource.getInvoiceByUUID(id = id)?.also {
            cacheHandler.set(
                key = it.id.toString(),
                serializer = InvoiceModel.serializer(),
                value = it
            )
        }
    }

    override suspend fun getInvoiceByExternalId(externalId: String): InvoiceModel? {
        return databaseSource.getInvoiceByExternalId(
            externalId = externalId
        )
    }

    override suspend fun getInvoices(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        userId: UUID,
    ): InvoiceListModel {
        val response = databaseSource.getInvoices(
            filters = GetInvoicesFilterData(
                senderCompanyName = filters.senderCompanyName,
                recipientCompanyName = filters.recipientCompanyName,
                minIssueDate = filters.minIssueDate,
                maxIssueDate = filters.maxIssueDate,
                minDueDate = filters.minDueDate,
                maxDueDate = filters.maxDueDate
            ),
            page = page,
            limit = limit,
            userId = userId
        )

        return InvoiceListModel(
            items = response.items,
            nextPage = response.nextPage,
            totalResults = response.totalResults
        )
    }

    override suspend fun delete(
        id: UUID
    ) {
        return databaseSource.delete(
            id = id
        ).also {
            cacheHandler.delete(
                key = id.toString(),
            )
        }
    }

    override suspend fun getInvoicesByBeneficiaryId(
        beneficiaryId: UUID,
        userId: UUID
    ): List<InvoiceListItemModel> {
        return databaseSource.getInvoicesByBeneficiaryId(
            beneficiaryId = beneficiaryId,
            userId = userId
        )
    }

    override suspend fun getInvoicesByIntermediaryId(
        intermediaryId: UUID,
        userId: UUID
    ): List<InvoiceListItemModel> {
        return databaseSource.getInvoicesByIntermediaryId(
            intermediaryId = intermediaryId,
            userId = userId
        )
    }
}
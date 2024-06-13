package invoicer.alaksiondev.com.datasource

import invoicer.alaksiondev.com.entities.InvoiceEntity
import invoicer.alaksiondev.com.entities.InvoiceTable
import invoicer.alaksiondev.com.models.createinvoice.CreateInvoiceModel
import invoices
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.entity.find
import java.util.UUID

internal interface InvoiceDataSource {
    suspend fun createInvoice(model: CreateInvoiceModel): String
    suspend fun getInvoiceByExternalId(externalId: String): InvoiceEntity?
}

internal class InvoiceDataSourceImpl(
    private val database: Database
) : InvoiceDataSource {

    override suspend fun createInvoice(model: CreateInvoiceModel): String {
        val key = database.insertAndGenerateKey(InvoiceTable) { invoiceTable ->
            set(invoiceTable.externalId, model.externalId)
            set(invoiceTable.senderCompanyName, model.senderCompanyName)
            set(invoiceTable.senderCompanyAddress, model.senderCompanyName)
            set(invoiceTable.recipientCompanyName, model.recipientCompanyName)
            set(invoiceTable.recipientCompanyAddress, model.recipientCompanyAddress)
            set(invoiceTable.issueDate, model.issueDate)
            set(invoiceTable.dueDate, model.dueDate)
            set(invoiceTable.beneficiaryName, model.beneficiary.beneficiaryName)
            set(invoiceTable.beneficiaryIban, model.beneficiary.beneficiaryIban)
            set(invoiceTable.beneficiarySwift, model.beneficiary.beneficiarySwift)
            set(invoiceTable.beneficiaryBankName, model.beneficiary.beneficiaryBankName)
            set(
                invoiceTable.beneficiaryBankAddress,
                model.beneficiary.beneficiaryBankAddress
            )

            set(invoiceTable.intermediaryIban, model.intermediary?.intermediaryIban)
            set(invoiceTable.intermediarySwift, model.intermediary?.intermediarySwift)
            set(invoiceTable.intermediaryBankName, model.intermediary?.intermediaryBankName)
            set(
                invoiceTable.intermediaryBankAddress,
                model.intermediary?.intermediaryBankAddress
            )
        }
        return (key as UUID).toString()
    }

    override suspend fun getInvoiceByExternalId(externalId: String): InvoiceEntity? {
        return database.invoices.find {
            it.externalId eq externalId
        }
    }
}
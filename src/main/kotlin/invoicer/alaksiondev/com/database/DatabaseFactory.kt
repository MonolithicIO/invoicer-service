import invoicer.alaksiondev.com.data.entities.InvoiceServiceTable
import invoicer.alaksiondev.com.data.entities.InvoiceTable
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf


object DatabaseFactory {
    val database by lazy {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/invoicer-api",
            driver = "org.postgresql.Driver",
            user = "invoicer",
            password = "1234"
        )
    }
}

val Database.invoices get() = this.sequenceOf(InvoiceTable)
val Database.invoiceServices get() = this.sequenceOf(InvoiceServiceTable)
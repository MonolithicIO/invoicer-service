import invoicer.alaksiondev.com.entities.InvoiceActivityTable
import invoicer.alaksiondev.com.entities.InvoiceTable
import io.github.cdimascio.dotenv.dotenv
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf


object DatabaseFactory {
    val connection by lazy {
        val env = dotenv()
        val database = env["DB_NAME"]
        val password = env["DB_PASSWORD"]
        val username = env["DB_USERNAME"]

        Database.connect(
            url = "jdbc:postgresql://localhost:5432/${database}",
            driver = "org.postgresql.Driver",
            user = username,
            password = password,
        )
    }

}

val Database.invoices get() = this.sequenceOf(InvoiceTable)
val Database.invoiceActivities get() = this.sequenceOf(InvoiceActivityTable)
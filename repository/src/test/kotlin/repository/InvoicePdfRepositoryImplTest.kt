package repository

import datasource.api.database.FakeInvoicePdfDatabaseSource
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class InvoicePdfRepositoryImplTest {

    private lateinit var databaseSource: FakeInvoicePdfDatabaseSource
    private lateinit var repository: InvoicePdfRepositoryImpl

    @BeforeTest
    fun setUp() {
        databaseSource = FakeInvoicePdfDatabaseSource()
        repository = InvoicePdfRepositoryImpl(databaseSource)
    }

    @Test
    fun `createInvoicePdf calls databaseSource with correct payload`() = runBlocking {
        val invoiceId = UUID.fromString("12345678-1234-5678-1234-567812345678")

        repository.createInvoicePdf(invoiceId)

        assertEquals(
            expected = 1,
            actual = databaseSource.createPdfCallStack.size
        )

        assertEquals(
            expected = invoiceId,
            actual = databaseSource.createPdfCallStack.first().invoiceId
        )

    }
}
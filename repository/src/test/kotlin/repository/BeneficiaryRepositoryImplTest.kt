package repository

import datasource.api.database.FakeBeneficiaryDatabaseSource
import foundation.cache.fakes.FakeCacheHandler
import kotlinx.coroutines.test.runTest
import models.beneficiary.CreateBeneficiaryModel
import models.fixtures.beneficiaryModelFixture
import models.fixtures.partialUpdateBeneficiaryModelFixture
import models.fixtures.userBeneficiariesFixture
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BeneficiaryRepositoryImplTest {
    private lateinit var repository: BeneficiaryRepositoryImpl
    private lateinit var datasource: FakeBeneficiaryDatabaseSource
    private lateinit var cache: FakeCacheHandler

    @BeforeTest
    fun setUp() {
        cache = FakeCacheHandler()
        datasource = FakeBeneficiaryDatabaseSource()

        repository = BeneficiaryRepositoryImpl(
            databaseSource = datasource,
            cacheHandler = cache,
        )
    }

    @Test
    fun `should call datasource and cache when deleting beneficiary`() = runTest {
        val beneficiaryId = UUID.fromString("12345678-1234-5678-1234-123456789012")
        datasource.deleteResponse = {}

        repository.delete(
            userId = UUID.randomUUID(),
            beneficiaryId = beneficiaryId
        )

        assertEquals(
            expected = 1,
            actual = cache.deleteCallStack.size
        )

        assertEquals(
            expected = beneficiaryId.toString(),
            actual = cache.deleteCallStack[0]
        )

        assertEquals(
            expected = 1,
            actual = datasource.deleteCalls
        )
    }

    @Test
    fun `should call datasource and cache when updating beneficiary`() = runTest {
        val beneficiaryId = UUID.fromString("12345678-1234-5678-1234-123456789012")
        datasource.deleteResponse = {}

        repository.update(
            userId = UUID.randomUUID(),
            beneficiaryId = beneficiaryId,
            model = partialUpdateBeneficiaryModelFixture
        )

        assertEquals(
            expected = 1,
            actual = cache.deleteCallStack.size
        )

        assertEquals(
            expected = 1,
            actual = datasource.updateCalls
        )

        assertEquals(
            expected = beneficiaryId.toString(),
            actual = cache.deleteCallStack[0]
        )
    }

    @Test
    fun `should return beneficiaries from datasource`() = runTest {
        datasource.getAllResponse = { userBeneficiariesFixture }

        val result = repository.getAll(
            userId = UUID.randomUUID(),
            page = 1,
            limit = 10
        )

        assertEquals(
            expected = userBeneficiariesFixture,
            actual = result
        )
    }

    @Test
    fun `should return beneficiary by swift from datasource`() = runTest {
        datasource.getBySwiftResponse = { beneficiaryModelFixture }

        val result = repository.getBySwift(
            userId = UUID.randomUUID(),
            swift = "123"
        )

        assertEquals(
            expected = beneficiaryModelFixture,
            actual = result
        )
    }

    @Test
    fun `should return beneficiary by id from cache`() = runTest {
        val beneficiaryUuid = UUID.fromString("12345678-1234-5678-1234-123456789012")
        cache.getCacheResponse = beneficiaryModelFixture

        val result = repository.getById(
            beneficiaryId = beneficiaryUuid
        )

        assertEquals(
            expected = beneficiaryModelFixture,
            actual = result
        )

        assertEquals(
            expected = 1,
            actual = cache.getCacheCalls
        )

        assertEquals(
            expected = 0,
            actual = datasource.getByIdCalls
        )
    }

    @Test
    fun `should return beneficiary by id from datasource`() = runTest {
        cache.getCacheResponse = null
        datasource.getBydIdResponse = { beneficiaryModelFixture }

        val result = repository.getById(
            beneficiaryId = beneficiaryModelFixture.id
        )

        assertEquals(
            expected = beneficiaryModelFixture,
            actual = result
        )

        assertEquals(
            expected = 1,
            actual = datasource.getByIdCalls
        )
    }

    @Test
    fun `should store beneficiary into cache when get by id is called`() = runTest {
        cache.getCacheResponse = null
        datasource.getBydIdResponse = { beneficiaryModelFixture }

        repository.getById(
            beneficiaryId = beneficiaryModelFixture.id
        )

        assertEquals(
            expected = Pair(
                beneficiaryModelFixture.id.toString(),
                beneficiaryModelFixture
            ),
            actual = cache.setCallStack.first()
        )

        assertEquals(
            expected = 1,
            actual = datasource.getByIdCalls
        )
    }

    @Test
    fun `should not store beneficiary into cache when get by id returns null`() = runTest {
        cache.getCacheResponse = null
        datasource.getBydIdResponse = { null }

        repository.getById(
            beneficiaryId = beneficiaryModelFixture.id
        )

        assertEquals(
            expected = 0,
            actual = cache.setCallStack.size
        )
    }

    @Test
    fun `should call datasource to create beneficiary`() = runTest {
        val userId = UUID.randomUUID()
        val createBeneficiaryModel = beneficiaryModelFixture

        datasource.createResponse = { "12345678-1234-5678-1234-123456789012" }

        val result = repository.create(
            userId = userId,
            model = CreateBeneficiaryModel(
                name = createBeneficiaryModel.name,
                iban = createBeneficiaryModel.iban,
                swift = createBeneficiaryModel.swift,
                bankName = createBeneficiaryModel.bankName,
                bankAddress = createBeneficiaryModel.bankAddress
            )
        )

        assertEquals(
            expected = "12345678-1234-5678-1234-123456789012",
            actual = result
        )

        assertEquals(
            expected = 1,
            actual = datasource.createCalls
        )
    }
}
package repository

import datasource.api.database.FakeIntermediaryDatabaseSource
import foundation.cache.fakes.FakeCacheHandler
import kotlinx.coroutines.test.runTest
import models.fixtures.intermediaryModelFixture
import models.fixtures.partialUpdateIntermediaryModelFixture
import models.fixtures.userIntermediariesFixture
import models.intermediary.CreateIntermediaryModel
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class IntermediaryRepositoryImplTest {
    private lateinit var repository: IntermediaryRepositoryImpl
    private lateinit var datasource: FakeIntermediaryDatabaseSource
    private lateinit var cache: FakeCacheHandler

    @BeforeTest
    fun setUp() {
        cache = FakeCacheHandler()
        datasource = FakeIntermediaryDatabaseSource()

        repository = IntermediaryRepositoryImpl(
            databaseSource = datasource,
            cacheHandler = cache,
        )
    }

    @Test
    fun `should call datasource and cache when deleting beneficiary`() = runTest {
        val intermediaryId = UUID.fromString("12345678-1234-5678-1234-123456789012")
        datasource.deleteResponse = {}

        repository.delete(
            userId = UUID.randomUUID(),
            intermediaryId = intermediaryId
        )

        assertEquals(
            expected = 1,
            actual = cache.deleteCallStack.size
        )

        assertEquals(
            expected = intermediaryId.toString(),
            actual = cache.deleteCallStack[0]
        )

        assertEquals(
            expected = 1,
            actual = datasource.deleteCalls
        )
    }

    @Test
    fun `should call datasource and cache when updating beneficiary`() = runTest {
        val intermediaryId = UUID.fromString("12345678-1234-5678-1234-123456789012")
        datasource.deleteResponse = {}

        repository.update(
            userId = UUID.randomUUID(),
            intermediaryId = intermediaryId,
            model = partialUpdateIntermediaryModelFixture
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
            expected = intermediaryId.toString(),
            actual = cache.deleteCallStack[0]
        )
    }

    @Test
    fun `should return beneficiaries from datasource`() = runTest {
        datasource.getAllResponse = { userIntermediariesFixture }

        val result = repository.getAll(
            userId = UUID.randomUUID(),
            page = 1,
            limit = 10
        )

        assertEquals(
            expected = userIntermediariesFixture,
            actual = result
        )
    }

    @Test
    fun `should return beneficiary by swift from datasource`() = runTest {
        datasource.getBySwiftResponse = { intermediaryModelFixture }

        val result = repository.getBySwift(
            userId = UUID.randomUUID(),
            swift = "123"
        )

        assertEquals(
            expected = intermediaryModelFixture,
            actual = result
        )
    }

    @Test
    fun `should return beneficiary by id from cache`() = runTest {
        val beneficiaryUuid = UUID.fromString("12345678-1234-5678-1234-123456789012")
        cache.getCacheResponse = intermediaryModelFixture

        val result = repository.getById(
            intermediaryId = beneficiaryUuid
        )

        assertEquals(
            expected = intermediaryModelFixture,
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
        datasource.getByIdResponse = { intermediaryModelFixture }

        val result = repository.getById(
            intermediaryId = intermediaryModelFixture.id
        )

        assertEquals(
            expected = intermediaryModelFixture,
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
        datasource.getByIdResponse = { intermediaryModelFixture }

        repository.getById(
            intermediaryId = intermediaryModelFixture.id
        )

        assertEquals(
            expected = Pair(
                intermediaryModelFixture.id.toString(),
                intermediaryModelFixture
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
        datasource.getByIdResponse = { null }

        repository.getById(
            intermediaryId = intermediaryModelFixture.id
        )

        assertEquals(
            expected = 0,
            actual = cache.setCallStack.size
        )
    }

    @Test
    fun `should call datasource to create beneficiary`() = runTest {
        val userId = UUID.randomUUID()
        val createBeneficiaryModel = intermediaryModelFixture

        datasource.createResponse = { "12345678-1234-5678-1234-123456789012" }

        val result = repository.create(
            userId = userId,
            model = CreateIntermediaryModel(
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
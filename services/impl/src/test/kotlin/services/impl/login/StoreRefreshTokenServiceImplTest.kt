package services.impl.login

import kotlinx.coroutines.test.runTest
import repository.test.repository.FakeRefreshTokenRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StoreRefreshTokenServiceImplTest {
    private lateinit var service: StoreRefreshTokenServiceImpl
    private lateinit var repository: FakeRefreshTokenRepository

    @BeforeTest
    fun setUp() {
        repository = FakeRefreshTokenRepository()
        service = StoreRefreshTokenServiceImpl(repository)
    }

    @Test
    fun `when service is called should store token`() = runTest {
        service.storeRefreshToken(token = "1234", userId = "12345")

        assertEquals(
            expected = 1,
            actual = repository.storeCalls
        )
    }
}
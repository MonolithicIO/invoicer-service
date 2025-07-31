package io.github.monolithic.invoicer.services.login

import kotlinx.coroutines.test.runTest
import io.github.monolithic.invoicer.repository.fakes.FakeRefreshTokenRepository
import io.github.monolithic.invoicer.utils.fakes.FakeClock
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StoreRefreshTokenServiceImplTest {
    private lateinit var service: StoreRefreshTokenServiceImpl
    private lateinit var repository: FakeRefreshTokenRepository

    @BeforeTest
    fun setUp() {
        repository = FakeRefreshTokenRepository()
        service = StoreRefreshTokenServiceImpl(
            refreshTokenRepository = repository,
            clock = FakeClock()
        )
    }

    @Test
    fun `when service is called should store token`() = runTest {
        service.createRefreshToken(token = "1234", userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))

        assertEquals(
            expected = 1,
            actual = repository.createCalls
        )
    }
}

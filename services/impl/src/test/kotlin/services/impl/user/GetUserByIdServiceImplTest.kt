package services.impl.user

import kotlinx.coroutines.test.runTest
import models.fixtures.userModelFixture
import repository.api.fakes.FakeUserRepository
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class GetUserByIdServiceImplTest {
    private lateinit var service: GetUserByIdServiceImpl
    private lateinit var repository: FakeUserRepository

    @BeforeTest
    fun setUp() {
        repository = FakeUserRepository()
        service = GetUserByIdServiceImpl(repository)
    }

    @Test
    fun `should return user from repository`() = runTest {
        val userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")

        val expectedUser = userModelFixture.copy(
            id = userId,
        )

        repository.getByEmailResponse = { expectedUser }

        val result = repository.getUserByEmail("email@gmail.com")

        assert(result == expectedUser)
    }
}
package io.github.alaksion.invoicer.services.user

import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import models.fixtures.userModelFixture
import repository.fakes.FakeUserRepository
import utils.exceptions.http.HttpCode
import utils.exceptions.http.HttpError

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

    @Test
    fun `should throw error if user is not found`() = runTest {
        val error = assertFailsWith<HttpError> {
            repository.getByIdResponse = { null }
            service.get(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
        }

        assertEquals(
            expected = HttpCode.NotFound,
            actual = error.statusCode
        )
    }
}

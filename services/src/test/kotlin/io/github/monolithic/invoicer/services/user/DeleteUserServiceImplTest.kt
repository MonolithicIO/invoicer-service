package io.github.monolithic.invoicer.services.user

import io.github.monolithic.invoicer.services.fakes.user.FakeGetUserByIdService
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import io.github.monolithic.invoicer.models.fixtures.userModelFixture
import io.github.monolithic.invoicer.repository.fakes.FakeUserRepository


class DeleteUserServiceImplTest {
    private lateinit var serviceImpl: DeleteUserServiceImpl
    private lateinit var userRepository: FakeUserRepository
    private lateinit var getUserByIdService: FakeGetUserByIdService

    @BeforeTest
    fun setUp() {
        userRepository = FakeUserRepository()
        getUserByIdService = FakeGetUserByIdService()

        serviceImpl = DeleteUserServiceImpl(
            userRepository = userRepository,
            getUserByIdService = getUserByIdService
        )
    }

    @Test
    fun `should delete user`() = runTest {
        val userUuid = "a0eebc4b-1f3d-4b2c-8f5d-7a2e6f9b8c3d"

        getUserByIdService.response = {
            userModelFixture.copy(
                id = UUID.fromString(userUuid)
            )
        }

        serviceImpl.delete(UUID.fromString(userUuid))

        assertEquals(
            expected = 1,
            actual = userRepository.deleteUserCallStack.size
        )

        assertEquals(
            expected = userUuid,
            actual = userRepository.deleteUserCallStack.first().toString()
        )
    }
}

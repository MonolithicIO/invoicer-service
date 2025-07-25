package io.github.monolithic.invoicer.services.fakes.user

import io.github.monolithic.invoicer.services.user.GetUserByEmailService
import java.util.*
import kotlinx.datetime.Instant
import io.github.monolithic.invoicer.models.user.UserModel

class FakeGetUserByEmailService : GetUserByEmailService {

    var response: suspend () -> UserModel? = { DEFAULT_RESPONSE }

    override suspend fun get(email: String): UserModel? {
        return response()
    }

    companion object {
        val DEFAULT_RESPONSE = UserModel(
            id = UUID.fromString("7956749e-9d8b-4ab7-abd1-29f0b7ecb9b8"),
            password = "1234",
            verified = true,
            email = "luccab.souza@gmail.com",
            createdAt = Instant.parse("2000-06-19T00:00:00Z"),
            updatedAt = Instant.parse("2000-06-19T00:00:00Z"),
            identityProviderUuid = null
        )
    }
}
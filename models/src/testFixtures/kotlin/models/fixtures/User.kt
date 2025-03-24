package models.fixtures

import kotlinx.datetime.Instant
import models.user.UserModel
import java.util.*

val userModelFixture = UserModel(
    id = UUID.randomUUID(),
    password = "examplePassword",
    verified = true,
    email = "example@example.com",
    createdAt = Instant.parse("2023-01-01T00:00:00Z"),
    updatedAt = Instant.parse("2023-01-01T00:00:00Z")
)
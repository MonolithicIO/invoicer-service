package models.fixtures

import kotlinx.datetime.Instant
import models.login.RefreshTokenModel

val refreshTokenModelFixture = RefreshTokenModel(
    userId = "user-123",
    token = "sample-token",
    createdAt = Instant.parse("2023-01-01T00:00:00Z"),
    updatedAt = Instant.parse("2023-01-01T00:00:00Z"),
    enabled = true
)
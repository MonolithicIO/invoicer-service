package io.github.monolithic.invoicer.models.fixtures

import kotlinx.datetime.Instant
import io.github.monolithic.invoicer.models.login.RefreshTokenModel
import java.util.*

val refreshTokenModelFixture = RefreshTokenModel(
    userId = UUID.fromString("user-123"),
    token = "sample-token",
    createdAt = Instant.parse("2023-01-01T00:00:00Z"),
    updatedAt = Instant.parse("2023-01-01T00:00:00Z"),
    enabled = true
)
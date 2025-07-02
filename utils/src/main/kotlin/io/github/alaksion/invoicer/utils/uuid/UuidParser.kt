package io.github.alaksion.invoicer.utils.uuid

import utils.exceptions.InvalidUUIDException
import java.util.*

fun parseUuid(string: String): UUID {
    return runCatching { UUID.fromString(string) }
        .fold(
            onSuccess = { it },
            onFailure = { throw InvalidUUIDException("Invalid UUID format: ${string}}") }
        )
}

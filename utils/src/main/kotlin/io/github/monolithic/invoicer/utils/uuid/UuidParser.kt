package io.github.monolithic.invoicer.utils.uuid

import io.github.monolithic.invoicer.foundation.exceptions.InvalidUUIDException
import java.util.*

fun parseUuid(string: String): UUID {
    return runCatching { UUID.fromString(string) }
        .fold(
            onSuccess = { it },
            onFailure = { throw InvalidUUIDException("Invalid UUID format: ${string}}") }
        )
}

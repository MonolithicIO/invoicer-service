package io.github.alaksion.invoicer.server.validation

import utils.exceptions.badRequestError

fun String?.requireFilledString(
    blankErrorMessage: String,
    missingErrorMessage: String = blankErrorMessage
): String {
    return this?.let {
        if (it.isBlank()) badRequestError(blankErrorMessage)
        it
    } ?: badRequestError(missingErrorMessage)
}
package controller.validation

import utils.exceptions.http.badRequestError

fun String?.requireFilledString(
    blankErrorMessage: String,
    missingErrorMessage: String = blankErrorMessage
): String {
    return this?.let {
        if (it.isBlank()) badRequestError(blankErrorMessage)
        it
    } ?: badRequestError(missingErrorMessage)
}

/***
 * Returns the value if it is not null, otherwise throws a bad request error with the provided message.
 */
fun requiredString(
    value: String?,
    missingErrorMessage: String
): String {
    return value ?: badRequestError(missingErrorMessage)
}
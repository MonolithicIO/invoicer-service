package controller.validation

import utils.exceptions.http.badRequestError

/***
 * Returns the value if it is not null, otherwise throws a bad request error with the provided message.
 */
fun requiredString(
    value: String?,
    missingErrorMessage: String
): String {
    return value ?: badRequestError(missingErrorMessage)
}
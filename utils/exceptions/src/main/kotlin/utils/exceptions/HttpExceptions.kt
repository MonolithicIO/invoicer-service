package utils.exceptions

import io.ktor.http.*

class HttpError(
    override val message: String,
    val statusCode: HttpStatusCode
) : Throwable()

fun httpError(message: String, code: HttpStatusCode): Nothing =
    throw HttpError(message, statusCode = code)

fun badRequestError(message: String): Nothing = httpError(message = message, code = HttpStatusCode.BadRequest)

package utils.exceptions.http

class HttpError(
    override val message: String,
    val statusCode: HttpCode
) : Throwable()

fun httpError(message: String, code: HttpCode): Nothing =
    throw HttpError(message, statusCode = code)

fun badRequestError(message: String): Nothing = httpError(message = message, code = HttpCode.BadRequest)

fun notFoundError(message: String): Nothing = httpError(message = message, code = HttpCode.NotFound)

fun unauthorizedResourceError(): Nothing =
    httpError(message = "User has no access to this resource", code = HttpCode.Forbidden)

fun unAuthorizedError(message: String): Nothing =
    httpError(message = message, code = HttpCode.UnAuthorized)

fun goneError(message: String): Nothing = httpError(message = message, code = HttpCode.Gone)

fun serverException(): Nothing =
    httpError(message = "Internal server error", code = HttpCode.ServerError)

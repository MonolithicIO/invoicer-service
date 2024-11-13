package utils.exceptions

class HttpError(
    override val message: String,
    val statusCode: HttpCode
) : Throwable()

fun httpError(message: String, code: HttpCode): Nothing =
    throw HttpError(message, statusCode = code)

fun badRequestError(message: String): Nothing = httpError(message = message, code = HttpCode.BadRequest)

fun notFoundError(message: String): Nothing = httpError(message = message, code = HttpCode.NotFound)

fun unauthorizedError(message: String = "Access denied"): Nothing = httpError(message = message, code = HttpCode.UnAuthorized)

fun unauthorizedResourceError(): Nothing =
    httpError(message = "User has no access to this resource", code = HttpCode.UnAuthorized)

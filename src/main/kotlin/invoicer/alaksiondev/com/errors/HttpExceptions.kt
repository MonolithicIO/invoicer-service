package invoicer.alaksiondev.com.errors

import io.ktor.http.HttpStatusCode

class HttpError(
    override val message: String,
    val statusCode: HttpStatusCode
) : Throwable()

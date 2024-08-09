package utils.authentication.api.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.datetime.toJavaInstant
import utils.authentication.api.AuthTokenGenerator
import utils.authentication.api.AuthTokenManager
import utils.date.api.DateProvider
import utils.exceptions.httpError
import kotlin.time.Duration.Companion.hours

internal class JwtTokenGenerator(
    private val dateProvider: DateProvider
) : AuthTokenGenerator {

    override fun generateToken(userId: String): String {
        val token = JWT.create()
            .withAudience(JWTConfig.AUDIENCE)
            .withIssuer(JWTConfig.ISSUER)
            .withClaim(JWTConfig.USER_ID_CLAIM, userId)
            .withExpiresAt(dateProvider.currentInstant().plus(24.hours).toJavaInstant())
            .sign(Algorithm.HMAC256(JWTConfig.SECRET))

        return token
    }
}

fun AuthenticationConfig.appJwt() {
    jwt("auth-jwt") {
        realm = JWTConfig.REALM
        verifier(
            JWT
                .require(Algorithm.HMAC256(JWTConfig.SECRET))
                .withAudience(JWTConfig.AUDIENCE)
                .withIssuer(JWTConfig.ISSUER)
                .build()
        )
        validate { token ->
            if (token.payload.getClaim(JWTConfig.USER_ID_CLAIM).asString().isNotBlank()) {
                JWTPrincipal(token.payload)
            } else {
                null
            }
        }
        challenge { _, _ ->
            httpError(
                message = AuthTokenManager.NOT_AUTHENTICATED_MESSAGE,
                code = HttpStatusCode.Unauthorized
            )
        }
    }
}

private object JWTConfig {
    const val AUDIENCE = "sample_audience"
    const val ISSUER = "sample_issuer"
    const val USER_ID_CLAIM = "userId"
    const val SECRET = "sample_secret"
    const val REALM = "invoicer"
    const val AUTH_NAME = "auth-jwt"
}


fun Route.jwtProtected(
    block: Route.() -> Unit
) {
    authenticate(JWTConfig.AUTH_NAME) {
        block()
    }
}

fun PipelineContext<Unit, ApplicationCall>.jwtUserId(): String {
    return call.principal<JWTPrincipal>()?.payload?.getClaim(JWTConfig.USER_ID_CLAIM)?.asString()
        ?: httpError(message = AuthTokenManager.NOT_AUTHENTICATED_MESSAGE, code = HttpStatusCode.Unauthorized)
}

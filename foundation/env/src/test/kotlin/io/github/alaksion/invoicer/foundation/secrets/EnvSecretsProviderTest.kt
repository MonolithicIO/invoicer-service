package io.github.alaksion.invoicer.foundation.secrets

import foundation.env.fakes.FakeInvoicerEnvironment
import io.github.alaksion.invoicer.foundation.env.secrets.EnvSecretsProvider
import io.github.alaksion.invoicer.foundation.env.secrets.SecretKeys
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class EnvSecretsProviderTest {

    private lateinit var provider: EnvSecretsProvider
    private lateinit var env: FakeInvoicerEnvironment

    @BeforeTest
    fun setUp() {
        env = FakeInvoicerEnvironment()
        provider = EnvSecretsProvider(env)
    }

    @Test
    fun retrievesDatabasePassword() {
        provider.getSecret(SecretKeys.DB_PASSWORD)
        assertEquals(expected = listOf("database.password"), actual = env.callStack)
    }

    @Test
    fun retrievesDatabaseUsername() {
        provider.getSecret(SecretKeys.DB_USERNAME)
        assertEquals(expected = listOf("database.username"), actual = env.callStack)
    }

    @Test
    fun retrievesDatabaseUrl() {
        provider.getSecret(SecretKeys.DB_URL)
        assertEquals(expected = listOf("database.url"), actual = env.callStack)
    }

    @Test
    fun retrievesJwtAudience() {
        provider.getSecret(SecretKeys.JWT_AUDIENCE)
        assertEquals(expected = listOf("jwt.audience"), actual = env.callStack)
    }

    @Test
    fun retrievesJwtIssuer() {
        provider.getSecret(SecretKeys.JWT_ISSUER)
        assertEquals(expected = listOf("jwt.issuer"), actual = env.callStack)
    }

    @Test
    fun retrievesJwtSecret() {
        provider.getSecret(SecretKeys.JWT_SECRET)
        assertEquals(expected = listOf("jwt.secret"), actual = env.callStack)
    }

    @Test
    fun retrievesJwtRealm() {
        provider.getSecret(SecretKeys.JWT_REALM)
        assertEquals(expected = listOf("jwt.realm"), actual = env.callStack)
    }

    @Test
    fun retrievesRedisHost() {
        provider.getSecret(SecretKeys.REDIS_HOST)
        assertEquals(expected = listOf("redis.host"), actual = env.callStack)
    }

    @Test
    fun retrievesRedisPort() {
        provider.getSecret(SecretKeys.REDIS_PORT)
        assertEquals(expected = listOf("redis.port"), actual = env.callStack)
    }

    @Test
    fun retrievesKafkaBootstrap() {
        provider.getSecret(SecretKeys.KAFKA_BOOTSTRAP)
        assertEquals(expected = listOf("kafka.bootstrap_servers"), actual = env.callStack)
    }

    @Test
    fun retrievesFirebaseId() {
        provider.getSecret(SecretKeys.FIREBASE_ID)
        assertEquals(expected = listOf("firebase.project_id"), actual = env.callStack)
    }

    @Test
    fun `should return empty string when environment variable is not set`() {
        env.response = null
        val result = provider.getSecret(SecretKeys.DB_PASSWORD)
        assertEquals("", result)
    }

    @Test
    fun `should return content when environment variable is set`() {
        env.response = "test_password"
        val result = provider.getSecret(SecretKeys.DB_PASSWORD)
        assertEquals("test_password", result)
    }
}
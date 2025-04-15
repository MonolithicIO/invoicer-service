plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
    `java-test-fixtures`
}

// Move to build plugin
group = "foundation.cache.impl"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(libs.kodein.server)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.coroutines.core)

    // Redis
    implementation(libs.jedis)

    // Project
    implementation(projects.foundation.exceptions)
    implementation(projects.foundation.secrets)
    implementation(projects.foundation.log)

    // Fixtures
    testFixturesImplementation(libs.kotlin.serialization)

    // Test
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(testFixtures(projects.foundation.log))
}
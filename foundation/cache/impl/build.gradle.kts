plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kover)
    alias(libs.plugins.serialization)
}

// Move to build plugin
group = "foundation.cache.impl"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(libs.kodein.server)
    implementation(libs.kotlin.serialization)

    // Redis
    implementation(libs.jedis)

    // Project
    implementation(projects.foundation.exceptions)
    implementation(projects.foundation.secrets)

    // Test
    testImplementation(libs.kotlin.test)
}
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

    // Project
    implementation(projects.foundation.date.impl)
    implementation(projects.foundation.exceptions)
    implementation(projects.foundation.secrets.impl)
    implementation(projects.foundation.redis.impl)

    // Test
    testImplementation(libs.kotlin.test)
}
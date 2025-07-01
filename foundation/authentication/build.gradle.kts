plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    `java-test-fixtures`
}

// Move to build plugin
group = "io.github.alaksion.invoicer.foundation.authentication.impl"
version = "0.0.1"

dependencies {
    // Kotlin
    implementation(libs.kotlin.datetime)
    implementation(libs.kotlin.coroutines.core)

    // Kodein
    implementation(libs.kodein.server)

    // Ktor
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.auth.core)
    implementation(libs.ktor.server.core.jvm)

    // Firebase
    implementation(libs.firebase.admin)

    // Project
    implementation(projects.utils)
    implementation(projects.foundation.log)
    implementation(projects.foundation.exceptions)
    implementation(projects.foundation.secrets)

    // Test
    testImplementation(libs.kotlin.test)
}
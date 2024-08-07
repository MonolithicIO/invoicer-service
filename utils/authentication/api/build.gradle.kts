plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.utils.authentication.api"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.auth.core)
    implementation(libs.ktor.server.core.jvm)
    implementation(libs.kodein.server)

    // Project
    implementation(projects.utils.date.api)
    implementation(projects.utils.exceptions)

    // Test
    testImplementation(libs.kotlin.test)
}
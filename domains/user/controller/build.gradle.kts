plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ktor)
}

group = "io.github.alaksion.invoicer.server.domains.user.controller"
version = "0.0.1"

dependencies {
    implementation(libs.bundles.ktor)
    implementation(libs.kodein.server)

    implementation(projects.domains.user.domain.api)
    implementation(projects.foundation.authentication.api)
    implementation(projects.foundation.exceptions)
}
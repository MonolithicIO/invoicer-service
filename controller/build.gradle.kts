plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
}

group = "io.github.alaksion.invoicer.controller"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(libs.bundles.ktor)
    implementation(projects.services.api)
    implementation(libs.kodein.server)
    implementation(projects.foundation.authentication.api)
    implementation(projects.models)
    implementation(projects.foundation.exceptions)
}

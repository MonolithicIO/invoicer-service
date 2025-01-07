import buildlogic.plugins.AppConfig

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
}

group = "io.github.alaksion.invoicer.foundation.env"
version = AppConfig.version

dependencies {
    implementation(libs.bundles.ktor)
    implementation(libs.kodein.server)
}
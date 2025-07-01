import buildlogic.plugins.AppConfig

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    `java-test-fixtures`
}

group = "io.github.alaksion.invoicer.foundation.env.impl"
version = AppConfig.version

dependencies {
    implementation(libs.bundles.ktor)
    implementation(libs.kodein.server)
    testImplementation(kotlin("test"))
}
plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    alias(libs.plugins.detekt)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.entities"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(libs.bundles.exposed)
}

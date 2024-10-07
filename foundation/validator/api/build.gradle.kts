plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kover)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.foundation.validator.api"
version = "0.0.1"

dependencies {
    implementation(libs.kodein.server)
}
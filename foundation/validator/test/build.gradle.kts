plugins {
    alias(libs.plugins.kotlin)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.foundation.validator.api"
version = "0.0.1"

dependencies {
    implementation(projects.foundation.validator.api)
}
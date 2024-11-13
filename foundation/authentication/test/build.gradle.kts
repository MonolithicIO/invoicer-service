plugins {
    alias(libs.plugins.kotlin)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.foundation.authentication.test"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(projects.foundation.authentication.api)
}
plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kover)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.service.api"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(projects.models)
}

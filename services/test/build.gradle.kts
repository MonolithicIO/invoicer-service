plugins {
    alias(libs.plugins.kotlin)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.service.test"
version = "0.0.1"

dependencies {
    implementation(projects.services.api)
    implementation(projects.models)
}
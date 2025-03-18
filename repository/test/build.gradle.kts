plugins {
    alias(libs.plugins.kotlin)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.repository.test"
version = "0.0.1"

dependencies {
    implementation(projects.repository.impl)
    implementation(projects.models)
    implementation(libs.kotlin.datetime)
}
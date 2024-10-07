plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kover)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.repository.api"
version = "0.0.1"

dependencies {
    implementation(projects.entities)
    implementation(projects.foundation.date.api)
    implementation(projects.models)

    implementation(libs.kodein.server)
    implementation(libs.kotlin.datetime)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
}

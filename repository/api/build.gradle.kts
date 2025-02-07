plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kover)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.repository.api"
version = "0.0.1"

dependencies {
    implementation(projects.entities)
    implementation(projects.models)
    implementation(projects.datasource.api)

    implementation(libs.exposed.dao)
    implementation(libs.kodein.server)
    implementation(libs.kotlin.datetime)
}

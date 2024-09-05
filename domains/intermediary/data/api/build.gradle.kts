plugins {
    alias(libs.plugins.kotlin)
}

group = "io.github.alaksion.invoicer.server.domains.intermediary.data.api"
version = "0.0.1"

dependencies {
    implementation(projects.domains.intermediary.domain.api)
    implementation(projects.entities)
    implementation(projects.foundation.date.api)
    implementation(libs.kodein.server)
    implementation(libs.bundles.exposed)
}
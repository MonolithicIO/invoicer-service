plugins {
    alias(libs.plugins.kotlin)
}

group = "io.github.alaksion.invoicer.server.domains.invoice.data.api"
version = "0.0.1"

dependencies {
    implementation(projects.domains.invoice.domain.api)
    implementation(projects.entities)
    implementation(libs.kodein.server)
    implementation(libs.bundles.exposed)
}
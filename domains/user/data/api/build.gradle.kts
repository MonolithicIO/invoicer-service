plugins {
    alias(libs.plugins.kotlin)
}

group = "io.github.alaksion.invoicer.server.domains.user.data.api"
version = "0.0.1"

dependencies {
    implementation(projects.domains.user.domain.api)
    implementation(projects.entities)
    implementation(libs.kodein.server)
    implementation(libs.bundles.exposed)
}
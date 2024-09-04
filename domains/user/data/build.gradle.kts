plugins {
    alias(libs.plugins.kotlin)
}

group = "io.github.alaksion.invoicer.server.domains.user.data"
version = "0.0.1"

dependencies {
    implementation(projects.domains.user.domain)
    implementation(projects.entities)
    implementation(libs.kodein.server)
    implementation(libs.bundles.exposed)
}
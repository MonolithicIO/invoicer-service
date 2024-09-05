plugins {
    alias(libs.plugins.kotlin)
}

group = "io.github.alaksion.invoicer.server.domains.invoice.domain.api"
version = "0.0.1"

dependencies {
    implementation(libs.kodein.server)
    implementation(libs.bundles.exposed)
}
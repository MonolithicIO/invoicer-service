plugins {
    alias(libs.plugins.kotlin)
}

group = "io.github.alaksion.invoicer.server.domains.invoice.controller"
version = "0.0.1"

dependencies {
    implementation(projects.domains.invoice.domain.api)
    implementation(libs.kodein.server)
}
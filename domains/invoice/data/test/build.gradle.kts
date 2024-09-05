plugins {
    alias(libs.plugins.kotlin)
}

group = "io.github.alaksion.invoicer.server.domains.invoice.data.test"
version = "0.0.1"

dependencies {
    implementation(projects.domains.invoice.data.api)
}
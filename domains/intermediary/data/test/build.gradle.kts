plugins {
    alias(libs.plugins.kotlin)
}

group = "io.github.alaksion.invoicer.server.domains.intermediary.data.test"
version = "0.0.1"

dependencies {
    implementation(projects.domains.intermediary.data.api)
}
plugins {
    alias(libs.plugins.kotlin)
}

group = "io.github.alaksion.invoicer.server.domains.intermediary.domain.api"
version = "0.0.1"

dependencies {
    implementation(projects.foundation.date.api)
    implementation(libs.kodein.server)
}
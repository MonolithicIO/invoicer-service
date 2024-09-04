plugins {
    alias(libs.plugins.kotlin)
}

group = "io.github.alaksion.invoicer.server.domains.user.domain"
version = "0.0.1"

dependencies {
    implementation(libs.kodein.server)
}
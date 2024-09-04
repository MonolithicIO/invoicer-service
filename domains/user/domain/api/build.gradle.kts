plugins {
    alias(libs.plugins.kotlin)
}

group = "io.github.alaksion.invoicer.server.domains.user.domain.api"
version = "0.0.1"

dependencies {
    implementation(libs.kodein.server)
    implementation(projects.foundation.password.api)
    implementation(projects.foundation.validator.api)
    implementation(projects.foundation.exceptions)
    implementation(libs.ktor.server.core.jvm)
}
plugins {
    alias(libs.plugins.kotlin)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.service.api"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(libs.kodein.server)

    implementation(projects.models)
    implementation(projects.repository.api)

    implementation(projects.foundation.validator.api)
    implementation(projects.foundation.exceptions)
    implementation(projects.foundation.date.api)
    implementation(projects.foundation.authentication.api)
    implementation(projects.foundation.password.api)
}

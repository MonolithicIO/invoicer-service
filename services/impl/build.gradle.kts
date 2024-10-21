plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kover)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.service.impl"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(libs.kodein.server)

    implementation(projects.models)
    implementation(projects.repository.api)
    implementation(projects.services.api)

    implementation(projects.foundation.validator.api)
    implementation(projects.foundation.exceptions)
    implementation(projects.foundation.date.api)
    implementation(projects.foundation.authentication.api)
    implementation(projects.foundation.password.api)
    implementation(libs.kotlin.coroutines.core)

    testImplementation(projects.repository.test)
    testImplementation(projects.foundation.validator.test)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(projects.services.test)
}

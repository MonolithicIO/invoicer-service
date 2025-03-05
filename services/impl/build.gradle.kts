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

    implementation(projects.foundation.validator.impl)
    implementation(projects.foundation.exceptions)
    implementation(projects.foundation.date.impl)
    implementation(projects.foundation.authentication.impl)
    implementation(projects.foundation.password.impl)
    implementation(projects.foundation.qrcode)
    implementation(libs.kotlin.coroutines.core)

    testImplementation(projects.repository.test)
    testImplementation(projects.foundation.validator.test)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(projects.services.test)
    testImplementation(projects.foundation.date.test)
    testImplementation(projects.foundation.password.test)
    testImplementation(projects.foundation.authentication.test)
}

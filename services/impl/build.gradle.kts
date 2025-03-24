plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kover)
    `java-test-fixtures`
}

// Move to build plugin
group = "io.github.alaksion.invoicer.service.impl"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(libs.kodein.server)

    implementation(projects.models)
    implementation(projects.repository)
    implementation(projects.services.api)

    implementation(libs.pdf.itext)
    implementation(projects.foundation.exceptions)
    implementation(projects.foundation.authentication.impl)
    implementation(projects.foundation.password.impl)
    implementation(projects.foundation.qrcode)
    implementation(projects.utils)
    implementation(libs.kotlin.coroutines.core)

    testImplementation(testFixtures(projects.models))
    testImplementation(testFixtures(projects.repository))
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(projects.services.test)
    testImplementation(projects.foundation.password.test)
    testImplementation(projects.foundation.authentication.test)
}

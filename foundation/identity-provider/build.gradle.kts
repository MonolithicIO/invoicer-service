plugins {
    alias(libs.plugins.kotlin)
    `java-test-fixtures`
}

group = "io.github.alaksion.invoicer.foundation.identityProvider"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kodein.server)
    implementation(libs.firebase.admin)

    implementation(projects.foundation.secrets)
    implementation(projects.foundation.log)

    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.kotlin.test)
}
plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
    `java-test-fixtures`
}

group = "io.github.monolithic.invoicer.consumers"
version = "0.0.1"

dependencies {
    implementation(projects.utils)
    implementation(projects.foundation.messaging)
    implementation(libs.kotlin.serialization)
    implementation(projects.services)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kodein.server)
    implementation(projects.foundation.log)
    implementation(libs.kotlin.datetime)
}

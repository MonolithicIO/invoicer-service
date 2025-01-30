plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kover)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.foundation.date.impl"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    testImplementation(libs.kotlin.test)
    implementation(libs.kodein.server)
}
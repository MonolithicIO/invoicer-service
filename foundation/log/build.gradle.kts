plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kover)
    `java-test-fixtures`
}

// Move to build plugin
group = "io.github.alaksion.invoicer.foundation.log"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kodein.server)

    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.kotlin.test)
}
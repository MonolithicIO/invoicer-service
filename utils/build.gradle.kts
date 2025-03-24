plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kover)
    `java-test-fixtures`
}

// Move to build plugin
group = "io.github.alaksion.invoicer.utils"
version = "0.0.1"

dependencies {
    implementation(libs.kodein.server)
    implementation(libs.kotlin.datetime)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.serialization)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.coroutines.test)

    testFixturesImplementation(libs.kotlin.datetime)
}

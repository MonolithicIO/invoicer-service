plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kover)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.foundation"
version = "0.0.1"

dependencies {
    implementation(libs.kodein.server)
    implementation(libs.kotlin.datetime)
    implementation(projects.models)
    implementation(libs.kotlin.coroutines.core)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.coroutines.test)
}

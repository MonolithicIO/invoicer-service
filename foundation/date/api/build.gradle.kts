plugins {
    alias(libs.plugins.kotlin)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.utils.date.api"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    testImplementation(libs.kotlin.test)
    implementation(libs.kodein.server)
}
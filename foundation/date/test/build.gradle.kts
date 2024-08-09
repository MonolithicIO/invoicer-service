plugins {
    alias(libs.plugins.kotlin)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.utils.date.test"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(projects.foundation.date.api)
}
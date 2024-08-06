plugins {
    alias(libs.plugins.kotlin)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.utils.password.test"
version = "0.0.1"

dependencies {
    implementation(projects.utils.password.api)
}
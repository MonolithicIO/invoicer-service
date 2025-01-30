plugins {
    alias(libs.plugins.kotlin)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.foundation.password.test"
version = "0.0.1"

dependencies {
    implementation(projects.foundation.password.impl)
}
plugins {
    alias(libs.plugins.kotlin)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.utils.password.api"
version = "0.0.1"

dependencies {
    implementation(libs.bcrypt)
    testImplementation(libs.kotlin.test)
    implementation(libs.kodein.server)
}
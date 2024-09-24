plugins {
    alias(libs.plugins.kotlin)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.utils.secrets.api"
version = "0.0.1"

dependencies {
    implementation(libs.kodein.server)
    implementation(libs.dotenv)
}
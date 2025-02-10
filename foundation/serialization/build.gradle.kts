plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kover)
    alias(libs.plugins.serialization)
}

// Move to build plugin
group = "foundation.cache.impl"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.serialization)
}
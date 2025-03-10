plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    alias(libs.plugins.kover)
}

// Move to build plugin
group = "foundation.events"
version = "0.0.1"
plugins {
    kotlin("jvm") version "2.0.0" apply false
    id("io.ktor.plugin") version "2.3.11" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0" apply false
    alias(libs.plugins.detekt) apply false
}
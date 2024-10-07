plugins {
    kotlin("jvm")
    alias(libs.plugins.kover)
}

dependencies {
    compileOnly(libs.detekt.api)
}
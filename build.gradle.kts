plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.detekt) apply false
    id("invoicer.detekt") apply false
    `version-catalog`
}

subprojects {
    apply(plugin = "invoicer.detekt")
}
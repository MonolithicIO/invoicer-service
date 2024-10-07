plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kover) apply false
    id("requireKover") apply false
    `version-catalog`
}

subprojects {
    apply(plugin = "requireKover")
}
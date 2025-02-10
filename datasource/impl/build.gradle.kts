plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kover)
}

// Move to build plugin
group = "datasource.impl"

dependencies {
    implementation(projects.datasource.api)

    implementation(projects.entities)
    implementation(projects.models)
    implementation(projects.foundation.date.impl)

    implementation(libs.kodein.server)
    implementation(libs.kotlin.datetime)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
}

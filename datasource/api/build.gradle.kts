plugins {
    alias(libs.plugins.kotlin)
}

// Move to build plugin
group = "datasource.api"

dependencies {
    implementation(projects.models)
    implementation(libs.kotlin.datetime)
}

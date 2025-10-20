plugins {
    alias(libs.plugins.kotlin)
    `java-test-fixtures`
}

// Move to build plugin
group = "io.github.monolithic.invoicer.foundation.storage"
version = "0.0.1"

dependencies {
    implementation(libs.aws.sdk)
    implementation(projects.foundation.log)
    implementation(projects.foundation.env)

    testImplementation(libs.kotlin.test)
    implementation(libs.kodein.server)
}
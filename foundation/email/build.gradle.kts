plugins {
    alias(libs.plugins.kotlin)
    `java-test-fixtures`
}

// Move to build plugin
group = "io.github.monolithic.invoicer.foundation.email"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(libs.resend)

    // DI
    implementation(libs.kodein.server)

    // Foundation
    implementation(projects.foundation.env)
    implementation(projects.foundation.log)

    // Test
    testImplementation(libs.kotlin.test)
}
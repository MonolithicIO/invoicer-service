plugins {
    alias(libs.plugins.kotlin)
    `java-test-fixtures`
}

// Move to build plugin
group = "io.github.monolithic.invoicer.foundation.messaging"
version = "0.0.1"

dependencies {
    implementation(libs.kafka)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kodein.server)

    implementation(projects.foundation.env)
    implementation(projects.foundation.log)

    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.kotlin.test)
}
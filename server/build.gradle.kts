plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    alias(libs.plugins.detekt)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.server"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(libs.bundles.exposed)
    implementation(libs.kodein.server)
    implementation(libs.kotlin.datetime)
    implementation(libs.openpdf)
    implementation(libs.dotenv)
    implementation(libs.postgres)
    implementation(libs.h2)
    implementation(libs.logback)
    implementation(libs.bundles.ktor)
    implementation("org.webjars:jquery:3.2.1")
    implementation(libs.swagger)

    // Foundation
    implementation(projects.foundation.password.api)
    implementation(projects.foundation.date.api)
    implementation(projects.foundation.authentication.api)
    implementation(projects.foundation.exceptions)
    implementation(projects.foundation.validator.api)

    // Entities
    implementation(projects.entities)

    testImplementation(libs.ktor.server.tests.jvm)
    testImplementation(libs.kotlin.test)
}

// Move to build plugin
detekt {
    val configPath = rootDir.absolutePath + "/config/detekt/detekt.yml"
    config.setFrom(configPath)
}

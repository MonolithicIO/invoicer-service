import buildlogic.plugins.AppConfig
import io.ktor.plugin.features.*

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.server"
version = AppConfig.version

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
    implementation(libs.postgres)
    implementation(libs.h2)
    implementation(libs.logback)
    implementation(libs.bundles.ktor)
    implementation("org.webjars:jquery:3.2.1")
    implementation(libs.swagger)

    // Foundation
    implementation(projects.foundation.password.api)
    kover(projects.foundation.password.api)

    implementation(projects.foundation.date.api)
    kover(projects.foundation.date.api)

    implementation(projects.foundation.authentication.api)
    kover(projects.foundation.authentication.api)

    implementation(projects.foundation.exceptions)
    kover(projects.foundation.exceptions)

    implementation(projects.foundation.validator.api)
    kover(projects.foundation.validator.api)

    implementation(projects.foundation.secrets.api)
    kover(projects.foundation.secrets.api)

    implementation(projects.foundation.env.api)
    kover(projects.foundation.env.api)

    // Repository
    implementation(projects.repository.api)
    kover(projects.repository.api)

    // Services
    implementation(projects.services.impl)
    kover(projects.services.impl)

    // Models
    implementation(projects.models)

    // Entities
    implementation(projects.entities)

    // Controller
    implementation(projects.controller)
    kover(projects.controller)

    testImplementation(libs.ktor.server.tests.jvm)
    testImplementation(libs.kotlin.test)
}

// Move to build plugin
detekt {
    val configPath = rootDir.absolutePath + "/config/detekt/detekt.yml"
    config.setFrom(configPath)
}

kover {
    reports {
        filters {
            excludes {
                packages("**.di")
            }
        }
    }
}

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_17)
        localImageName.set("alaksion/invoicer-api")
        imageTag.set(AppConfig.version)

        externalRegistry.set(
            DockerImageRegistry.dockerHub(
                appName = provider { "invoicer-api" },
                username = providers.environmentVariable("DOCKER_USERNAME"),
                password = providers.environmentVariable("DOCKER_PASSWORD")
            )
        )
    }
}
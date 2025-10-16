enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.jetbrains.kotlinx.kover.aggregation") version "0.9.3"
}

kover {
    enableCoverage()
    reports {
        excludesAnnotatedBy = setOf("io.github.monolithic.invoicer.utils.annotations.IgnoreCoverage")
        excludedClasses = setOf("**.di", "**.fakes", "**.fixtures", "**.pdfwriter.itext")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}

rootProject.name = "invoicer-api"
include(":server")
include(":foundation:password")
include(":foundation:authentication")
include(":foundation:exceptions")
include(":foundation:messaging")
include(":foundation:log")
include(":services")
include(":repository")
include(":consumers")
include(":models")
include(":controller")
include(":foundation:env")
include(":foundation:cache")
include(":foundation:qrcode")
include(":foundation:storage")
include(":foundation:email")
include(":utils")
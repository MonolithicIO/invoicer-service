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
include(":services:api")
include(":services:impl")
include(":repository")
include(":consumers")
include(":models")
include(":controller")
include(":foundation:env")
include(":foundation:cache")
include(":foundation:qrcode")
include(":foundation:storage")
include(":utils")
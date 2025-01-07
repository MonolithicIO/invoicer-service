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
include(":detekt")
include(":foundation:password:api")
include(":foundation:password:test")
include(":foundation:authentication:api")
include(":foundation:authentication:test")
include(":foundation:date:api")
include(":foundation:date:test")
include(":foundation:exceptions")
include(":entities")
include(":foundation:validator:api")
include(":foundation:validator:test")
include(":foundation:secrets:api")
include(":foundation:secrets:test")
include(":services:api")
include(":services:impl")
include(":services:test")
include(":repository:api")
include(":repository:test")
include(":models")
include(":controller")
include(":foundation:env:api")
include(":foundation:env:test")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
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
include(":utils:password:api")
include(":utils:password:test")
include(":utils:authentication:api")
include(":utils:authentication:test")
include(":utils:date:api")
include(":utils:date:test")
include(":utils:exceptions")
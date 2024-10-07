plugins {
    `kotlin-dsl`
    `version-catalog`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kover.gradle)
}

gradlePlugin {
    plugins {
        create("require-cover-plugin") {
            id = "requireKover"
            implementationClass = "buildlogic.plugins.RequireKoverPlugin"
        }
    }
}
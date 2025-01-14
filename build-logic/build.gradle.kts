plugins {
    `kotlin-dsl`
    `version-catalog`
    `java-gradle-plugin`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kover.gradle)
    implementation(libs.detekt.gradle)
}

gradlePlugin {
    plugins {
        create("require-cover-plugin") {
            id = "requireKover"
            implementationClass = "buildlogic.plugins.RequireKoverPlugin"
        }
        create("invoicer-detekt") {
            id = "invoicer.detekt"
            implementationClass = "buildlogic.plugins.InvoicerDetektPlugin"
        }
    }
}
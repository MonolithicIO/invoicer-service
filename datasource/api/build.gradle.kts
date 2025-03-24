plugins {
    alias(libs.plugins.kotlin)
    `java-test-fixtures`
}

// Move to build plugin
group = "datasource.api"

dependencies {
    implementation(projects.models)
    implementation(libs.kotlin.datetime)

    // Fixtures
    testFixturesImplementation(testFixtures(projects.models))
}

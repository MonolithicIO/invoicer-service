plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
}

// Move to build plugin
group = "foundation.redis.impl"

dependencies {
    implementation(libs.kodein.server)
    implementation(libs.jedis)
    implementation(projects.foundation.secrets.impl)
}

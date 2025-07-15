package io.github.monolithic.invoicer.server.app.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
val jsonConfig = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    explicitNulls = true
}

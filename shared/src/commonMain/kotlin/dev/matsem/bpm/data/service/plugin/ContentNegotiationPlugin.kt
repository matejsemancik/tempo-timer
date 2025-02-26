package dev.matsem.bpm.data.service.plugin

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class ContentNegotiationPlugin : BaseHttpClientPlugin {

    override fun install(receiver: HttpClientConfig<*>) = receiver.install(ContentNegotiation) {
        json(
            json = Json {
                encodeDefaults = true
                isLenient = true
                prettyPrint = false
                ignoreUnknownKeys = true
                explicitNulls = false
                coerceInputValues = true
            },
            contentType = ContentType.Application.Json
        )
    }
}
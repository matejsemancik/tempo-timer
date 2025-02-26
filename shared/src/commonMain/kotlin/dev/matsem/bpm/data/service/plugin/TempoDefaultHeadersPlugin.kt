package dev.matsem.bpm.data.service.plugin

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders

internal class TempoDefaultHeadersPlugin : BaseHttpClientPlugin {

    override fun install(receiver: HttpClientConfig<*>) = receiver.install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
}
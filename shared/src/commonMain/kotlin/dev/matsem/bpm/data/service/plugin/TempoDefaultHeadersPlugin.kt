package dev.matsem.bpm.data.service.plugin

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*

internal class TempoDefaultHeadersPlugin : BaseHttpClientPlugin {

    override fun install(receiver: HttpClientConfig<*>) = receiver.install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
}
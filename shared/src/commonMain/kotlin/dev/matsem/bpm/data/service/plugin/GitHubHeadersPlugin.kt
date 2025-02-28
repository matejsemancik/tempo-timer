package dev.matsem.bpm.data.service.plugin

import dev.matsem.bpm.tooling.Platform
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders

internal class GitHubHeadersPlugin(private val platform: Platform) : BaseHttpClientPlugin {

    override fun install(receiver: HttpClientConfig<*>) = receiver.install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        header(HttpHeaders.Accept, "application/vnd.github+json")
        header(HttpHeaders.UserAgent, "tempo-timer/${platform.getVersionString()}")
        header("X-GitHub-Api-Version", "2022-11-28")
    }
}
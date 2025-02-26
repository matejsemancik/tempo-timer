package dev.matsem.bpm.data.service.plugin

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic

internal class JiraAuthPlugin(
    private val email: String,
    private val apiToken: String
) : BaseHttpClientPlugin {
    override fun install(receiver: HttpClientConfig<*>) = receiver.install(Auth) {
        basic {
            sendWithoutRequest { true }
            credentials {
                BasicAuthCredentials(
                    username = email,
                    password = apiToken
                )
            }
        }
    }
}
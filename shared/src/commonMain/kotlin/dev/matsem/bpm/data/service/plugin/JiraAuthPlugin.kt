package dev.matsem.bpm.data.service.plugin

import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*

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
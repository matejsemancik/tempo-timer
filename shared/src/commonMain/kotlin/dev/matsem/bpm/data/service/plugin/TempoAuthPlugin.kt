package dev.matsem.bpm.data.service.plugin

import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*

internal class TempoAuthPlugin(
    private val accessToken: String,
) : BaseHttpClientPlugin {
    override fun install(receiver: HttpClientConfig<*>) = receiver.install(Auth) {
        bearer {
            sendWithoutRequest { true }
            loadTokens {
                BearerTokens(
                    accessToken = accessToken,
                    refreshToken = null
                )
            }
        }
    }
}
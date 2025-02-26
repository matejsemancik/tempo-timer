package dev.matsem.bpm.data.service.plugin

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer

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
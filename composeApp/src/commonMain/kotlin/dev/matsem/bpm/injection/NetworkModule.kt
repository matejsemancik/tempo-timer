package dev.matsem.bpm.injection

import de.jensklingenberg.ktorfit.Ktorfit
import dev.matsem.bpm.data.model.domain.Credentials
import dev.matsem.bpm.data.service.JiraApi
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

private object JiraClient
private object TempoClient

fun networkModule() = module {
    includes(jiraClientModule())
}

private fun jiraClientModule() = module {
    scope<Credentials> {
        scoped<HttpClient>(named<JiraClient>()) {
            val credentials: Credentials = get()

            HttpClient {
                install(ContentNegotiation) {
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
                install(DefaultRequest) {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                }
                install(Auth) {
                    basic {
                        sendWithoutRequest { true }
                        credentials {
                            BasicAuthCredentials(
                                username = credentials.email,
                                password = credentials.jiraApiKey
                            )
                        }
                    }
                }
                install(Logging) {
                    this.logger = object : Logger {
                        override fun log(message: String) {
                            println(message)
                        }
                    }
                    this.level = LogLevel.ALL
                }
            }
        }

        scoped<Ktorfit>(named<JiraClient>()) {
            val credentials: Credentials = get()

            Ktorfit.Builder()
                .baseUrl(credentials.baseUrl)
                .httpClient(get<HttpClient>(named<JiraClient>()))
                .build()
        }

        scoped<JiraApi> {
            val ktorfit: Ktorfit = get(named<JiraClient>())
            @Suppress("DEPRECATION")
            ktorfit.create<JiraApi>()
        }
    }
}

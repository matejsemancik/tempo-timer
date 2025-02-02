package dev.matsem.bpm.injection

import de.jensklingenberg.ktorfit.Ktorfit
import dev.matsem.bpm.data.model.domain.Credentials
import dev.matsem.bpm.data.service.JiraApi
import dev.matsem.bpm.data.service.plugin.ContentNegotiationPlugin
import dev.matsem.bpm.data.service.plugin.JiraAuthPlugin
import dev.matsem.bpm.data.service.plugin.LoggingPlugin
import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun networkModule() = module {
    includes(httpClientPluginsModule(), jiraClientModule())
}

private fun httpClientPluginsModule() = module {
    factory { ContentNegotiationPlugin() }
    factory { (logLevel: LogLevel) -> LoggingPlugin(logLevel) }
    factory { (email: String, apiToken: String) -> JiraAuthPlugin(email, apiToken) }
}

private fun jiraClientModule() = module {
    scope<Credentials> {

        scoped<HttpClient>(named<JiraApi>()) {
            val credentials: Credentials = get()
            getNativeHttpClient {
                get<ContentNegotiationPlugin>()
                    .install(this)
                get<JiraAuthPlugin>(parameters = { parametersOf(credentials.email, credentials.jiraApiToken) })
                    .install(this)
                get<LoggingPlugin>(parameters = { parametersOf(LogLevel.BODY) })
                    .install(this)
            }
        }

        scoped<Ktorfit>(named<JiraApi>()) {
            val credentials: Credentials = get()

            Ktorfit.Builder()
                .baseUrl(credentials.baseUrl)
                .httpClient(get<HttpClient>(named<JiraApi>()))
                .build()
        }

        scoped<JiraApi> {
            val ktorfit: Ktorfit = get(named<JiraApi>())
            @Suppress("DEPRECATION")
            ktorfit.create<JiraApi>()
        }
    }
}

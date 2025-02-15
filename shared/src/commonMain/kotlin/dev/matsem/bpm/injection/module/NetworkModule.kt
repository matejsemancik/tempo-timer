package dev.matsem.bpm.injection.module

import de.jensklingenberg.ktorfit.Ktorfit
import dev.matsem.bpm.data.repo.model.Credentials
import dev.matsem.bpm.data.service.jira.JiraApi
import dev.matsem.bpm.data.service.jira.JiraApiManager
import dev.matsem.bpm.data.service.jira.JiraApiManagerImpl
import dev.matsem.bpm.data.service.plugin.*
import dev.matsem.bpm.data.service.plugin.ContentNegotiationPlugin
import dev.matsem.bpm.data.service.plugin.JiraAuthPlugin
import dev.matsem.bpm.data.service.plugin.LoggingPlugin
import dev.matsem.bpm.data.service.plugin.TempoAuthPlugin
import dev.matsem.bpm.data.service.tempo.TempoApi
import dev.matsem.bpm.data.service.tempo.TempoApiManager
import dev.matsem.bpm.data.service.tempo.TempoApiManagerImpl
import dev.matsem.bpm.tooling.Constants
import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

internal expect fun platformNetworkModule(): Module

internal fun networkModule() = module {
    includes(platformNetworkModule())
    includes(httpClientPluginsModule(), apiClientsModule())
}

private fun httpClientPluginsModule() = module {
    factory { ContentNegotiationPlugin() }
    factory { (logLevel: LogLevel) -> LoggingPlugin(logLevel) }
    factory { (email: String, apiToken: String) -> JiraAuthPlugin(email, apiToken) }
    factory { (apiToken: String) -> TempoAuthPlugin(apiToken) }
    factory { TempoDefaultHeadersPlugin() }
}

private fun apiClientsModule() = module {

    singleOf(::JiraApiManagerImpl) bind JiraApiManager::class
    singleOf(::TempoApiManagerImpl) bind TempoApiManager::class

    scope<Credentials> {

        // region JIRA --------------------------------------------------------

        // Jira HTTP client
        scoped<HttpClient>(named<JiraApi>()) {
            val credentials: Credentials = get()
            val plugins = listOf(
                get<ContentNegotiationPlugin>(),
                get<JiraAuthPlugin>(parameters = { parametersOf(credentials.email, credentials.jiraApiToken) }),
                get<LoggingPlugin>(parameters = { parametersOf(LogLevel.BODY) })
            )

            get<HttpClient>(parameters = { parametersOf(plugins) })
        }

        // Jira Ktorfit
        scoped<Ktorfit>(named<JiraApi>()) {
            val credentials: Credentials = get()

            Ktorfit.Builder()
                .baseUrl(Constants.JiraApiUrl(credentials.jiraDomain))
                .httpClient(get<HttpClient>(named<JiraApi>()))
                .build()
        }

        // JiraApi
        scoped<JiraApi> {
            val ktorfit: Ktorfit = get(named<JiraApi>())
            @Suppress("DEPRECATION")
            ktorfit.create()
        }

        // endregion

        // region TEMPO --------------------------------------------------------

        // Tempo API client
        scoped<HttpClient>(named<TempoApi>()) {
            val credentials: Credentials = get()
            val plugins = listOf(
                get<ContentNegotiationPlugin>(),
                get<TempoAuthPlugin>(parameters = { parametersOf(credentials.tempoApiToken) }),
                get<LoggingPlugin>(parameters = { parametersOf(LogLevel.BODY) }),
                get<TempoDefaultHeadersPlugin>()
            )

            get<HttpClient>(parameters = { parametersOf(plugins) })
        }

        // Tempo Ktorfit
        scoped<Ktorfit>(named<TempoApi>()) {
            Ktorfit.Builder()
                .baseUrl(Constants.TempoApiUrl)
                .httpClient(get<HttpClient>(named<TempoApi>()))
                .build()
        }

        // TempoApi
        scoped<TempoApi> {
            val ktorfit: Ktorfit = get(named<TempoApi>())
            @Suppress("DEPRECATION")
            ktorfit.create()
        }

        // endregion
    }
}

package dev.matsem.bpm.injection.module

import de.jensklingenberg.ktorfit.Ktorfit
import dev.matsem.bpm.data.model.domain.Credentials
import dev.matsem.bpm.data.service.JiraApi
import dev.matsem.bpm.data.service.JiraApiManager
import dev.matsem.bpm.data.service.JiraApiManagerImpl
import dev.matsem.bpm.data.service.plugin.ContentNegotiationPlugin
import dev.matsem.bpm.data.service.plugin.JiraAuthPlugin
import dev.matsem.bpm.data.service.plugin.LoggingPlugin
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
            val plugins = listOf(
                get<ContentNegotiationPlugin>(),
                get<JiraAuthPlugin>(parameters = { parametersOf(credentials.email, credentials.jiraApiToken) }),
                get<LoggingPlugin>(parameters = { parametersOf(LogLevel.BODY) })
            )

            get<HttpClient>(
                parameters = { parametersOf(plugins) }
            )
        }

        scoped<Ktorfit>(named<JiraApi>()) {
            val credentials: Credentials = get()

            Ktorfit.Builder()
                .baseUrl(credentials.jiraApiHost)
                .httpClient(get<HttpClient>(named<JiraApi>()))
                .build()
        }

        scoped<JiraApi> {
            val ktorfit: Ktorfit = get(named<JiraApi>())
            @Suppress("DEPRECATION")
            ktorfit.create()
        }
    }

    singleOf(::JiraApiManagerImpl) bind JiraApiManager::class
}

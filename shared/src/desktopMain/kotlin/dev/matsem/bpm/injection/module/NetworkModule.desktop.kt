package dev.matsem.bpm.injection.module

import dev.matsem.bpm.data.service.plugin.BaseHttpClientPlugin
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun platformNetworkModule(): Module = module {
    factory<HttpClient> { (plugins: List<BaseHttpClientPlugin>) ->
        HttpClient(OkHttp) {
            plugins.forEach { it.install(this) }
            engine {
                preconfigured = OkHttpClient.Builder().build()
            }
        }
    }
}
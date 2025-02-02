package dev.matsem.bpm.injection

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import okhttp3.OkHttpClient

actual fun getNativeHttpClient(
    configurePlugins: HttpClientConfig<*>.() -> Unit
): HttpClient = HttpClient(OkHttp) {
    configurePlugins()
    engine {
        preconfigured = OkHttpClient.Builder().build()
    }
}
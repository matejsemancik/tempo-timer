package dev.matsem.bpm.injection

import io.ktor.client.*

expect fun getNativeHttpClient(configurePlugins: HttpClientConfig<*>.() -> Unit): HttpClient
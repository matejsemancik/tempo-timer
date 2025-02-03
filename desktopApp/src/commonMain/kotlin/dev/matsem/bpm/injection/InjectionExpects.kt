package dev.matsem.bpm.injection

import io.ktor.client.*
import okio.Path

expect fun getNativeHttpClient(configurePlugins: HttpClientConfig<*>.() -> Unit): HttpClient

expect fun getDatastorePath(): String
package dev.matsem.bpm.data.service.plugin

import io.ktor.client.*
import io.ktor.client.plugins.logging.*

internal class LoggingPlugin(private val logLevel: LogLevel) : BaseHttpClientPlugin {
    override fun install(receiver: HttpClientConfig<*>) = receiver.install(Logging) {
        logger = object : Logger {
            override fun log(message: String) = println(message)
        }
        level = logLevel
    }
}
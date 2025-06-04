package dev.matsem.bpm.data.service.plugin

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.LoggingFormat

internal class LoggingPlugin(private val logLevel: LogLevel) : BaseHttpClientPlugin {
    override fun install(receiver: HttpClientConfig<*>) = receiver.install(Logging) {
        logger = object : Logger {
            override fun log(message: String) = println(message)
        }
        level = logLevel
        format = LoggingFormat.OkHttp
    }
}

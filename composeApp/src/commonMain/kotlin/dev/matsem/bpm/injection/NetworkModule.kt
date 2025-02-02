package dev.matsem.bpm.injection

import de.jensklingenberg.ktorfit.Ktorfit
import dev.matsem.bpm.data.model.domain.Credentials
import org.koin.dsl.module

fun networkModule() = module {
    scope<Credentials> {
        scoped { (baseUrl: String) -> createKtorfitClient(baseUrl) }
    }
}

private fun createKtorfitClient(baseUrl: String): Ktorfit {
    return Ktorfit.Builder()
        .baseUrl(baseUrl)
        .build()
}

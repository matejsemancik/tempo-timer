package dev.matsem.bpm.data.model.domain

import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope

data class Credentials(
    val baseUrl: String,
    val email: String,
    val jiraApiKey: String,
    val tempoApiKey: String
) : KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }
}
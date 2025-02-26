package dev.matsem.bpm.data.mapping

import dev.matsem.bpm.data.persistence.model.Credentials as Credentials_Persistence
import dev.matsem.bpm.data.repo.model.Credentials as Credentials_Domain

internal object CredentialsMapping {

    fun Credentials_Domain.toPersistenceModel() = Credentials_Persistence(
        jiraDomain = jiraDomain,
        email = email,
        jiraApiToken = jiraApiToken,
        tempoApiToken = tempoApiToken
    )

    fun Credentials_Persistence.toDomainModel() = Credentials_Domain(
        jiraDomain = jiraDomain,
        email = email,
        jiraApiToken = jiraApiToken,
        tempoApiToken = tempoApiToken
    )
}
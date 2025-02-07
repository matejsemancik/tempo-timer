package dev.matsem.bpm.data.model.mapping

import dev.matsem.bpm.data.model.domain.Credentials as Credentials_Domain
import dev.matsem.bpm.data.model.persistence.Credentials as Credentials_Persistence

internal object CredentialsMapping {

    fun Credentials_Domain.toPersistenceModel() = Credentials_Persistence(
        jiraCloudName = jiraCloudName,
        email = email,
        jiraApiToken = jiraApiToken,
        tempoApiToken = tempoApiToken
    )

    fun Credentials_Persistence.toDomainModel() = Credentials_Domain(
        jiraCloudName = jiraCloudName,
        email = email,
        jiraApiToken = jiraApiToken,
        tempoApiToken = tempoApiToken
    )
}
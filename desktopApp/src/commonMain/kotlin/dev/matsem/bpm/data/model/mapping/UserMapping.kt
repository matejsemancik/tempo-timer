package dev.matsem.bpm.data.model.mapping

import dev.matsem.bpm.data.model.domain.User
import dev.matsem.bpm.data.model.network.jira.JiraUser

object UserMapping {

    fun JiraUser.toDomainModel() = User(
        email = emailAddress,
        displayName = displayName,
        avatarUrl = avatarUrls.largestUrl
    )
}
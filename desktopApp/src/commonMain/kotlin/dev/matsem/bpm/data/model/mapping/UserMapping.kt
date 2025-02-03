package dev.matsem.bpm.data.model.mapping

import dev.matsem.bpm.data.model.domain.User as User_Domain
import dev.matsem.bpm.data.model.network.jira.JiraUser as JiraUser_Network

object UserMapping {

    fun JiraUser_Network.toDomainModel() = User_Domain(
        email = emailAddress,
        displayName = displayName,
        avatarUrl = avatarUrls.largestUrl
    )
}
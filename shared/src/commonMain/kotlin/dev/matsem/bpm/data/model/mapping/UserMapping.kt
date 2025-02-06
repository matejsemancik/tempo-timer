package dev.matsem.bpm.data.model.mapping

import dev.matsem.bpm.data.model.database.User as User_Db
import dev.matsem.bpm.data.model.domain.User as User_Domain
import dev.matsem.bpm.data.model.network.jira.JiraUser as JiraUser_Network

object UserMapping {

    fun JiraUser_Network.toDbInsert() = User_Db(
        email = emailAddress,
        displayName = displayName,
        avatarUrl = avatarUrls.largestUrl
    )

    fun User_Db.toDomainModel() = User_Domain(
        email = email,
        displayName = displayName,
        avatarUrl = avatarUrl
    )
}
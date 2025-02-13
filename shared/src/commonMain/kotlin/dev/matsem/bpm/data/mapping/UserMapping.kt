package dev.matsem.bpm.data.mapping

import dev.matsem.bpm.data.database.model.User as User_Db
import dev.matsem.bpm.data.repo.model.User as User_Domain
import dev.matsem.bpm.data.service.jira.model.user.JiraUser as JiraUser_Network

internal object UserMapping {

    fun JiraUser_Network.toDbInsert() = User_Db(
        accountId = accountId,
        email = emailAddress,
        displayName = displayName,
        avatarUrl = avatarUrls.largestUrl
    )

    fun User_Db.toDomainModel() = User_Domain(
        accountId = accountId,
        email = email,
        displayName = displayName,
        avatarUrl = avatarUrl
    )
}
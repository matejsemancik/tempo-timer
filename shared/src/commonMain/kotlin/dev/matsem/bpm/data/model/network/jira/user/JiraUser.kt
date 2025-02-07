package dev.matsem.bpm.data.model.network.jira.user

import dev.matsem.bpm.data.model.network.jira.user.AvatarUrlsBean
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JiraUser(

    @SerialName("accountId")
    val accountId: String,

    @SerialName("emailAddress")
    val emailAddress: String,

    @SerialName("avatarUrls")
    val avatarUrls: AvatarUrlsBean,

    @SerialName("displayName")
    val displayName: String
)
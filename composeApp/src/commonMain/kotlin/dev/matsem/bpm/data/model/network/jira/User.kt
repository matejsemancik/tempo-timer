package dev.matsem.bpm.data.model.network.jira

import dev.matsem.bpm.data.model.network.jira.AvatarUrlsBean
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(

    @SerialName("accountId")
    val accountId: String,

    @SerialName("emailAddress")
    val emailAddress: String,

    @SerialName("avatarUrls")
    val avatarUrls: AvatarUrlsBean,

    @SerialName("displayUrl")
    val displayName: String
)
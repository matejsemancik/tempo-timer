package dev.matsem.bpm.data.model.network.jira

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvatarUrlsBean(

    @SerialName("48x48")
    val largestUrl: String
)
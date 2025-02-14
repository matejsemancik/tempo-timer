package dev.matsem.bpm.data.service.tempo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageableTempoResponse<T : Any>(
    @SerialName("metadata")
    val metadata: PageableTempoMetadata,

    @SerialName("results")
    val results: List<T>,

    @SerialName("self")
    val self: String
)

@Serializable
data class PageableTempoMetadata(

    @SerialName("count")
    val count: Int,

    @SerialName("offset")
    val offset: Int,

    @SerialName("limit")
    val limit: Int,

    @SerialName("next")
    val next: String? = null,

    @SerialName("previous")
    val previous: String? = null
)
package dev.matsem.bpm.data.service.tempo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TempoResultsResponse<T : Any>(

    @SerialName("metadata")
    val metadata: TempoResultsMetadata,

    @SerialName("results")
    val results: List<T>,

    @SerialName("self")
    val self: String,
)

@Serializable
data class TempoResultsMetadata(

    @SerialName("count")
    val count: Int,
)
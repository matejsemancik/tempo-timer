package dev.matsem.bpm.data.service.tempo.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Worklog(

    @SerialName("createdAt")
    val createdAt: Instant,

    @SerialName("description")
    val description: String? = null,

    @SerialName("issue")
    val issue: Issue,

    @SerialName("startDate")
    val startDate: LocalDate,

    @SerialName("startDateTimeUtc")
    val startDateTimeUtc: Instant? = null,

    @SerialName("tempoWorklogId")
    val tempoWorklogId: Long,

    @SerialName("timeSpentSeconds")
    val timeSpentSeconds: Long,

    @SerialName("billableSeconds")
    val billableSeconds: Long? = null,

    @SerialName("updatedAt")
    val updatedAt: Instant,
) {
    @Serializable
    data class User(
        @SerialName("accountId")
        val accountId: String,
        @SerialName("self")
        val self: String,
    )

    @Serializable
    data class Issue(
        @SerialName("id")
        val id: Long,
        @SerialName("self")
        val self: String,
    )
}
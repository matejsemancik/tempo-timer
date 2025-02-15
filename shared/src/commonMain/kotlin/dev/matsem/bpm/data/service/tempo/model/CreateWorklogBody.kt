package dev.matsem.bpm.data.service.tempo.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateWorklogBody(

    @SerialName("authorAccountId")
    val jiraAccountId: String,

    @SerialName("issueId")
    val jiraIssueId: Long,

    @SerialName("startDate")
    val startedAtDate: LocalDate,

    @SerialName("startTime")
    val startedAtTime: LocalTime? = null,

    @SerialName("timeSpentSeconds")
    val timeSpentSeconds: Long,

    @SerialName("description")
    val description: String? = null,
) {
    init {
        require(timeSpentSeconds >= 60) { "Duration must be at least one minute" }
    }
}
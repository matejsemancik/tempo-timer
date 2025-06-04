package dev.matsem.bpm.data.service.tempo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PeriodsResponse(
    @SerialName("periods")
    val periods: List<TimesheetApprovalPeriod>,
)

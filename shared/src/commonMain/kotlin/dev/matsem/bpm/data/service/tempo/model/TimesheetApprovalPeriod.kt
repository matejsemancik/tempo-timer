package dev.matsem.bpm.data.service.tempo.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimesheetApprovalPeriod(
    @SerialName("from")
    val from: LocalDate,
    @SerialName("to")
    val to: LocalDate,
)

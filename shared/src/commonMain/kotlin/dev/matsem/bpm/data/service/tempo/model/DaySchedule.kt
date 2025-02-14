package dev.matsem.bpm.data.service.tempo.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DaySchedule(

    @SerialName("date")
    val date: LocalDate,

    @SerialName("requiredSeconds")
    val requiredSeconds: Long,

    @SerialName("type")
    val type: Type
) {

    enum class Type {
        WORKING_DAY, NON_WORKING_DAY, HOLIDAY, HOLIDAY_AND_NON_WORKING_DAY
    }
}
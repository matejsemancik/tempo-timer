package dev.matsem.bpm.data.repo.model

data class Issue(
    val id: Long,
    val key: String,
    val summary: String,
    val iconUrl: String,
)

val MockIssues = listOf(
    Issue(
        id = 0L,
        key = "MTSM-1",
        summary = "Payment button",
        iconUrl = "",
    ),
    Issue(
        id = 0L,
        key = "MTSM-4",
        summary = "Spraviť robotu",
        iconUrl = "",
    ),
    Issue(
        id = 0L,
        key = "MTSM-19",
        summary = "[AN] Spraviť robotu",
        iconUrl = "",
    ),
    Issue(
        id = 0L,
        key = "MTSM-140",
        summary = "Robenie roboty",
        iconUrl = "",
    ),
    Issue(
        id = 0L,
        key = "MTSM-5",
        summary = "Tempo Desktop",
        iconUrl = "",
    )
)

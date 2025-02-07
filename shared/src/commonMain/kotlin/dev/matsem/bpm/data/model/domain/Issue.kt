package dev.matsem.bpm.data.model.domain

data class Issue(
    val key: String,
    val title: String,
    val iconUrl: String,
)

val MockIssues = listOf(
    Issue(
        key = "MTSM-1",
        title = "Payment button",
        iconUrl = "",
    ),
    Issue(
        key = "MTSM-4",
        title = "Spraviť robotu",
        iconUrl = "",
    ),
    Issue(
        key = "MTSM-19",
        title = "[AN] Spraviť robotu",
        iconUrl = "",
    ),
    Issue(
        key = "MTSM-140",
        title = "Robenie roboty",
        iconUrl = "",
    ),
    Issue(
        key = "MTSM-5",
        title = "Tempo Desktop",
        iconUrl = "",
    )
)

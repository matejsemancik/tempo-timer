package dev.matsem.bpm.tooling

object Constants {

    val JiraApiUrl: (domain: String) -> String = { domain -> "https://${domain}.atlassian.net/" }
    const val TempoApiUrl = "https://api.tempo.io/4/"
}
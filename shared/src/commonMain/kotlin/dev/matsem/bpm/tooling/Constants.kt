package dev.matsem.bpm.tooling

object Constants {

    val JIRA_API_URL: (domain: String) -> String = { domain -> "https://${domain}.atlassian.net/" }
    const val TEMPO_API_URL = "https://api.tempo.io/4/"
}
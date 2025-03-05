package dev.matsem.bpm.tooling

object Constants {

    val JiraApiUrl: (domain: String) -> String = { domain -> "https://${domain}.atlassian.net/" }
    val JiraIssueUrl: (apiUrl: String, issueKey: String) -> String = { apiUrl, key -> "${apiUrl}browse/$key"}

    const val TempoApiUrl = "https://api.tempo.io/4/"
    const val GitHubApiUrl = "https://api.github.com/"
    const val LatestReleaseUrl = "https://github.com/matejsemancik/tempo-timer/releases/latest"
}

package dev.matsem.bpm.feature.settings.presentation

interface SettingsActions {
    fun onJiraCloudName(input: String)
    fun onJiraEmailInput(input: String)
    fun onJiraApiKeyInput(input: String)
    fun onTempoApiKeyInput(input: String)

    fun onLoginClick()
    fun onLogoutClick()

    companion object {
        fun noOp() = object : SettingsActions {
            override fun onJiraCloudName(input: String) = Unit
            override fun onJiraEmailInput(input: String) = Unit
            override fun onJiraApiKeyInput(input: String) = Unit
            override fun onTempoApiKeyInput(input: String) = Unit
            override fun onLoginClick() = Unit
            override fun onLogoutClick() = Unit
        }
    }
}
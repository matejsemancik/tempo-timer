package dev.matsem.bpm.feature.settings.presentation

import androidx.compose.ui.text.input.TextFieldValue

interface SettingsActions {
    fun onJiraCloudName(input: TextFieldValue)
    fun onJiraEmailInput(input: TextFieldValue)
    fun onJiraApiKeyInput(input: TextFieldValue)
    fun onTempoApiKeyInput(input: TextFieldValue)

    fun onLoginClick()
    fun onLogoutClick()

    companion object {
        fun noOp() = object : SettingsActions {
            override fun onJiraCloudName(input: TextFieldValue) = Unit
            override fun onJiraEmailInput(input: TextFieldValue) = Unit
            override fun onJiraApiKeyInput(input: TextFieldValue) = Unit
            override fun onTempoApiKeyInput(input: TextFieldValue) = Unit
            override fun onLoginClick() = Unit
            override fun onLogoutClick() = Unit
        }
    }
}
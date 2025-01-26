package dev.matsem.bpm.feature.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.feature.settings.presentation.SettingsActions
import dev.matsem.bpm.feature.settings.presentation.SettingsScreen
import dev.matsem.bpm.feature.settings.presentation.SettingsState
import dev.matsem.bpm.feature.settings.ui.widget.SettingsTextField
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreenUi(
    screen: SettingsScreen,
    modifier: Modifier = Modifier
) {
    val state by screen.state.collectAsStateWithLifecycle()
    SettingsScreenUi(state, screen.actions, modifier)
}

@Composable
fun SettingsScreenUi(
    state: SettingsState,
    actions: SettingsActions,
    modifier: Modifier = Modifier
) {
    Column(modifier.verticalScroll(rememberScrollState())) {
        Text(
            "🔐 Credentials",
            style = BpmTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3)
        )
        VerticalSpacer(Grid.d1)
        Text(
            "Sign In by providing necessary credentials.",
            style = BpmTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3)
        )
        VerticalSpacer(Grid.d1)
        Text(
            "Jira API key is used to sync your profile and search issues. Tempo API key is used to synchronize worklogs with Tempo.\nInstructions on how to generate TBD.",
            style = BpmTheme.typography.labelMedium,
            modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3)
        )
        VerticalSpacer(Grid.d2)
        SettingsTextField(
            label = "Jira URL",
            value = state.jiraUrl,
            onValueChanged = actions::onJiraUrlInput,
            modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3),
            prefix = { Text("https://") },
            suffix = { Text(".atlassian.net") }
        )
        SettingsTextField(
            label = "Atlassian account e-mail",
            value = state.jiraEmail,
            onValueChanged = actions::onJiraEmailInput,
            modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3),
        )
        SettingsTextField(
            label = "Jira API key",
            value = state.jiraApiKey,
            onValueChanged = actions::onJiraApiKeyInput,
            isPassword = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3),
        )
        SettingsTextField(
            label = "Tempo API key",
            value = state.tempoApiKey,
            onValueChanged = actions::onTempoApiKeyInput,
            isPassword = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3),
        )

        Row(modifier = Modifier.fillMaxWidth().padding(Grid.d3), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = {},
                shape = BpmTheme.shapes.small
            ) {
                Text("Sign In")
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenUiPreview() {
    Showcase {
        SettingsScreenUi(
            SettingsState(),
            object : SettingsActions {
                override fun onJiraUrlInput(input: String) = Unit
                override fun onJiraEmailInput(input: String) = Unit
                override fun onJiraApiKeyInput(input: String) = Unit
                override fun onTempoApiKeyInput(input: String) = Unit
            },
            Modifier.fillMaxWidth()
        )
    }
}
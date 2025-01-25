package dev.matsem.bpm.feature.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.HorizontalSpacer
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.feature.settings.presentation.SettingsActions
import dev.matsem.bpm.feature.settings.presentation.SettingsModel
import dev.matsem.bpm.feature.settings.presentation.SettingsScreen
import dev.matsem.bpm.feature.settings.presentation.SettingsState
import dev.matsem.bpm.feature.settings.ui.widget.ApiKeyTextField
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
            "🔐 Application credentials",
            style = BpmTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3)
        )
        VerticalSpacer(Grid.d1)
        Text(
            "We need Jira API key to search issues. Tempo API key is used to synchronize workflogs to Tempo.\n\nYou can generate your own API key for Jira [here](https://example.com) and for Tempo [here](https://example.com).",
            style = BpmTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3)
        )
        VerticalSpacer(Grid.d2)
        ApiKeyTextField(
            label = "Jira API key",
            value = state.jiraApiKey,
            onValueChanged = actions::onJiraApiKeyInput,
            modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3),
        )
        ApiKeyTextField(
            label = "Tempo API key",
            value = state.tempoApiKey,
            onValueChanged = actions::onTempoApiKeyInput,
            modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3),
        )

        Row(modifier = Modifier.fillMaxWidth().padding(Grid.d3), horizontalArrangement = Arrangement.End) {
            OutlinedButton(
                onClick = {}
            ) {
                Text("Sign out")
            }
            HorizontalSpacer(Grid.d2)
            Button(
                onClick = {}
            ) {
                Text("Save")
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
                override fun onJiraApiKeyInput(input: String) = Unit
                override fun onTempoApiKeyInput(input: String) = Unit
            },
            Modifier.fillMaxWidth()
        )
    }
}
package dev.matsem.bpm.feature.settings.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.matsem.bpm.data.model.domain.User
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.feature.settings.presentation.SettingsActions
import dev.matsem.bpm.feature.settings.presentation.SettingsScreen
import dev.matsem.bpm.feature.settings.presentation.SettingsState
import dev.matsem.bpm.feature.settings.ui.widget.SettingsTextField
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun SettingsScreenUi(
    modifier: Modifier = Modifier,
    screen: SettingsScreen = koinInject(),
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
        when (state) {
            is SettingsState.SignedOut -> SignedOutSection(state, actions)
            is SettingsState.SignedIn -> SignedInSection(state, actions)
        }
    }
}

@Composable
private fun ColumnScope.SignedOutSection(
    state: SettingsState.SignedOut,
    actions: SettingsActions,
    modifier: Modifier = Modifier
) {
    Text(
        "Sign In by providing necessary credentials.",
        style = BpmTheme.typography.bodyMedium,
        modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3)
    )
    VerticalSpacer(Grid.d1)
    Text(
        "Jira API token is used to sync your profile and search issues. Tempo API token is used to synchronize worklogs with Tempo.\nInstructions on how to generate TBD.",
        style = BpmTheme.typography.labelMedium,
        modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3)
    )
    VerticalSpacer(Grid.d2)
    SettingsTextField(
        label = "Jira URL",
        value = state.jiraHostname,
        onValueChanged = actions::onJiraHostnameInput,
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
        label = "Jira API token",
        value = state.jiraApiToken,
        onValueChanged = actions::onJiraApiKeyInput,
        isPassword = true,
        modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3),
    )
    SettingsTextField(
        label = "Tempo API token",
        value = state.tempoApiToken,
        onValueChanged = actions::onTempoApiKeyInput,
        isPassword = true,
        modifier = Modifier.fillMaxWidth().padding(horizontal = Grid.d3),
    )

    Row(modifier = Modifier.fillMaxWidth().padding(Grid.d3), horizontalArrangement = Arrangement.End) {
        Button(
            onClick = actions::onLoginClick,
            shape = BpmTheme.shapes.small
        ) {
            Text("Sign In")
        }
    }
}

@Composable
private fun ColumnScope.SignedInSection(
    state: SettingsState.SignedIn,
    actions: SettingsActions,
    modifier: Modifier = Modifier
) {
    Row(Modifier.fillMaxWidth().padding(horizontal = Grid.d3), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Signed in as: ${state.user.displayName}",
            modifier = Modifier.weight(1f),
        )
        OutlinedButton(
            onClick = actions::onLogoutClick,
            shape = BpmTheme.shapes.small
        ) {
            Text("Sign Out")
        }
    }
    VerticalSpacer(Grid.d4)
}

@Preview
@Composable
fun SettingsScreenUiPreview_SignedOut() {
    Showcase {
        SettingsScreenUi(
            SettingsState.SignedOut(),
            object : SettingsActions {
                override fun onJiraHostnameInput(input: String) = Unit
                override fun onJiraEmailInput(input: String) = Unit
                override fun onJiraApiKeyInput(input: String) = Unit
                override fun onTempoApiKeyInput(input: String) = Unit
                override fun onLoginClick() = Unit
                override fun onLogoutClick() = Unit
            },
            Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun SettingsScreenUiPreview_SignedIn() {
    Showcase {
        SettingsScreenUi(
            SettingsState.SignedIn(User(email = "me@matsem.dev", displayName = "Janko Mrkvička", avatarUrl = "")),
            object : SettingsActions {
                override fun onJiraHostnameInput(input: String) = Unit
                override fun onJiraEmailInput(input: String) = Unit
                override fun onJiraApiKeyInput(input: String) = Unit
                override fun onTempoApiKeyInput(input: String) = Unit
                override fun onLoginClick() = Unit
                override fun onLogoutClick() = Unit
            },
            Modifier.fillMaxWidth()
        )
    }
}
package dev.matsem.bpm.feature.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import dev.matsem.bpm.data.model.domain.User
import dev.matsem.bpm.design.input.DesignButton
import dev.matsem.bpm.design.input.DesignOutlinedButton
import dev.matsem.bpm.design.input.DesignTextField
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.HorizontalSpacer
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.design.tooling.centeredVertically
import dev.matsem.bpm.feature.settings.presentation.SettingsActions
import dev.matsem.bpm.feature.settings.presentation.SettingsScreen
import dev.matsem.bpm.feature.settings.presentation.SettingsState
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
        text = "🔐 Credentials",
        style = BpmTheme.typography.titleMedium,
        modifier = Modifier.fillMaxWidth().padding(horizontal = BpmTheme.dimensions.horizontalContentPadding)
    )
    VerticalSpacer(Grid.d1)
    Text(
        text = "Sign In by providing necessary credentials.",
        style = BpmTheme.typography.bodyMedium,
        modifier = Modifier.fillMaxWidth().padding(horizontal = BpmTheme.dimensions.horizontalContentPadding)
    )
    VerticalSpacer(Grid.d1)
    Text(
        text = "Jira API token is used to sync your profile and search issues. Tempo API token is used to synchronize worklogs with Tempo.\nInstructions on how to generate TBD.",
        style = BpmTheme.typography.labelMedium,
        modifier = Modifier.fillMaxWidth().padding(horizontal = BpmTheme.dimensions.horizontalContentPadding)
    )
    VerticalSpacer(Grid.d2)
    DesignTextField(
        label = "Jira URL",
        value = state.jiraHostname,
        onValueChanged = actions::onJiraHostnameInput,
        modifier = Modifier.fillMaxWidth().padding(horizontal = BpmTheme.dimensions.horizontalContentPadding),
        prefix = { Text("https://") },
        suffix = { Text(".atlassian.net") }
    )
    DesignTextField(
        label = "Atlassian account e-mail",
        value = state.jiraEmail,
        onValueChanged = actions::onJiraEmailInput,
        modifier = Modifier.fillMaxWidth().padding(horizontal = BpmTheme.dimensions.horizontalContentPadding),
    )
    DesignTextField(
        label = "Jira API token",
        value = state.jiraApiToken,
        onValueChanged = actions::onJiraApiKeyInput,
        isPassword = true,
        modifier = Modifier.fillMaxWidth().padding(horizontal = BpmTheme.dimensions.horizontalContentPadding),
    )
    DesignTextField(
        label = "Tempo API token",
        value = state.tempoApiToken,
        onValueChanged = actions::onTempoApiKeyInput,
        isPassword = true,
        modifier = Modifier.fillMaxWidth().padding(horizontal = BpmTheme.dimensions.horizontalContentPadding),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BpmTheme.dimensions.horizontalContentPadding, vertical = Grid.d3),
        horizontalArrangement = Arrangement.End
    ) {
        DesignButton(
            text = "Sign In",
            isLoading = state.isLoading,
            onClick = actions::onLoginClick,
        )
    }
}

@Composable
private fun ColumnScope.SignedInSection(
    state: SettingsState.SignedIn,
    actions: SettingsActions,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = BpmTheme.dimensions.horizontalContentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = state.user.avatarUrl,
            contentDescription = null,
            modifier = Modifier
                .size(Grid.d9)
                .clip(CircleShape)
                .background(BpmTheme.colorScheme.inverseOnSurface)
        )
        HorizontalSpacer(Grid.d2)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = state.user.displayName,
                style = BpmTheme.typography.bodyLarge.centeredVertically(),
                color = BpmTheme.colorScheme.onSurface
            )
            VerticalSpacer(Grid.d0_5)
            Text(
                text = state.user.email,
                style = BpmTheme.typography.labelLarge.centeredVertically(),
                color = BpmTheme.colorScheme.outline
            )
        }
        DesignOutlinedButton(
            text = "Sign Out",
            onClick = actions::onLogoutClick,
        )
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
            SettingsState.SignedIn(
                User(
                    email = "emanuel@bacigala.sk",
                    displayName = "Emanuel Bacigala",
                    avatarUrl = ""
                )
            ),
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
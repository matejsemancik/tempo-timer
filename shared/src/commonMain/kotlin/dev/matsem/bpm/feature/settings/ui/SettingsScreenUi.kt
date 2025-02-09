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
import dev.matsem.bpm.data.repo.model.User
import dev.matsem.bpm.design.input.AppButton
import dev.matsem.bpm.design.input.AppOutlinedButton
import dev.matsem.bpm.design.input.AppTextField
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
        VerticalSpacer(Grid.d3)
    }
}

@Composable
private fun LabeledTextField(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(text = label, style = BpmTheme.typography.labelMedium)
        VerticalSpacer(Grid.d1)
        content()
    }
}

@Composable
private fun SignedOutSection(
    state: SettingsState.SignedOut,
    actions: SettingsActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = BpmTheme.dimensions.horizontalContentPadding)
    ) {
        Text(
            text = "🔐 Credentials",
            style = BpmTheme.typography.titleMedium,
        )
        VerticalSpacer(Grid.d1)
        Text(
            text = "Sign In by providing necessary credentials.",
            style = BpmTheme.typography.bodyMedium,
        )
        VerticalSpacer(Grid.d1)
        Text(
            text = "Jira API token is used to sync your profile and search issues. Tempo API token is used to synchronize worklogs with Tempo.\nInstructions on how to generate TBD.",
            style = BpmTheme.typography.labelMedium,
        )

        VerticalSpacer(Grid.d2)
        LabeledTextField("Jira URL") {
            AppTextField(
                value = state.jiraCloudName,
                onValueChange = actions::onJiraCloudName,
                prefix = "https://",
                suffix = ".atlassian.net",
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        VerticalSpacer(Grid.d2)
        LabeledTextField("Atlassian account e-mail") {
            AppTextField(
                value = state.jiraEmail,
                onValueChange = actions::onJiraEmailInput,
                singleLine = true,
                placeholder = "you@corporate.org",
                modifier = Modifier.fillMaxWidth(),
            )
        }

        VerticalSpacer(Grid.d2)
        LabeledTextField("Jira API token") {
            PasswordTextField(
                value = state.jiraApiToken,
                onValueChange = actions::onJiraApiKeyInput,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        VerticalSpacer(Grid.d2)
        LabeledTextField("Tempo API token") {
            PasswordTextField(
                value = state.tempoApiToken,
                onValueChange = actions::onTempoApiKeyInput,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        VerticalSpacer(Grid.d3)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            AppButton(
                text = "Sign In",
                isLoading = state.isLoading,
                onClick = actions::onLoginClick,
            )
        }
    }
}

@Composable
private fun SignedInSection(
    state: SettingsState.SignedIn,
    actions: SettingsActions,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = BpmTheme.dimensions.horizontalContentPadding),
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
        AppOutlinedButton(
            text = "Sign Out",
            onClick = actions::onLogoutClick,
        )
    }
}

@Preview
@Composable
fun SettingsScreenUiPreview_SignedOut() {
    Showcase {
        SettingsScreenUi(
            SettingsState.SignedOut(),
            SettingsActions.noOp(),
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
            SettingsActions.noOp(),
            Modifier.fillMaxWidth()
        )
    }
}
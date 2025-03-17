package dev.matsem.bpm.feature.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bpm_tracker.shared.generated.resources.Res
import bpm_tracker.shared.generated.resources.credentials_description
import bpm_tracker.shared.generated.resources.credentials_instructions_md
import bpm_tracker.shared.generated.resources.jira_api_token
import bpm_tracker.shared.generated.resources.jira_email
import bpm_tracker.shared.generated.resources.jira_email_placeholder
import bpm_tracker.shared.generated.resources.jira_url
import bpm_tracker.shared.generated.resources.settings_account_section_title
import bpm_tracker.shared.generated.resources.settings_credentials_section_title
import bpm_tracker.shared.generated.resources.sign_in
import bpm_tracker.shared.generated.resources.sign_out
import bpm_tracker.shared.generated.resources.tempo_api_token
import coil3.compose.AsyncImage
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownTypography
import dev.matsem.bpm.data.repo.model.User
import dev.matsem.bpm.design.button.AppButton
import dev.matsem.bpm.design.button.AppOutlinedButton
import dev.matsem.bpm.design.input.AppTextField
import dev.matsem.bpm.design.input.LabeledTextField
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.HorizontalSpacer
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.design.tooling.centeredVertically
import dev.matsem.bpm.feature.settings.presentation.SettingsActions
import dev.matsem.bpm.feature.settings.presentation.SettingsScreen
import dev.matsem.bpm.feature.settings.presentation.SettingsState
import org.jetbrains.compose.resources.stringResource
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
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    Box {
        Column(modifier.verticalScroll(scrollState)) {
            VerticalSpacer(Grid.d3)
            when (state) {
                is SettingsState.SignedOut -> SignedOutSection(state, actions)
                is SettingsState.SignedIn -> SignedInSection(state, actions)
            }
        }
    }
}

@Composable
private fun SignedOutSection(
    state: SettingsState.SignedOut,
    actions: SettingsActions,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = BpmTheme.dimensions.horizontalContentPadding)
    ) {
        Text(
            text = stringResource(Res.string.settings_credentials_section_title),
            style = BpmTheme.typography.titleMedium,
        )
        VerticalSpacer(Grid.d1)
        Text(
            text = stringResource(Res.string.credentials_description),
            style = BpmTheme.typography.bodyMedium,
        )
        VerticalSpacer(Grid.d1)
        Markdown(
            content = stringResource(Res.string.credentials_instructions_md),
            typography = markdownTypography(
                paragraph = BpmTheme.typography.labelMedium,
                link = BpmTheme.typography.labelMedium.copy(textDecoration = TextDecoration.Underline)
            )
        )
        VerticalSpacer(Grid.d2)
        LabeledTextField(stringResource(Res.string.jira_url)) {
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
        LabeledTextField(stringResource(Res.string.jira_email)) {
            AppTextField(
                value = state.jiraEmail,
                onValueChange = actions::onJiraEmailInput,
                singleLine = true,
                placeholder = stringResource(Res.string.jira_email_placeholder),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        VerticalSpacer(Grid.d2)
        LabeledTextField(stringResource(Res.string.jira_api_token)) {
            PasswordTextField(
                value = state.jiraApiToken,
                onValueChange = actions::onJiraApiKeyInput,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        VerticalSpacer(Grid.d2)
        LabeledTextField(stringResource(Res.string.tempo_api_token)) {
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
                text = stringResource(Res.string.sign_in),
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
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth().padding(horizontal = BpmTheme.dimensions.horizontalContentPadding)) {
        Text(
            stringResource(Res.string.settings_account_section_title),
            style = BpmTheme.typography.titleMedium,
        )
        VerticalSpacer(Grid.d2)
        Row(
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
                text = stringResource(Res.string.sign_out),
                onClick = actions::onLogoutClick,
            )
        }
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
                    accountId = "emanuel",
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

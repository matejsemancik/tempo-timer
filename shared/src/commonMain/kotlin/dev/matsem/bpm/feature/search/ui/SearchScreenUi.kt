package dev.matsem.bpm.feature.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.matsem.bpm.design.input.DesignTextField
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.feature.search.presentation.SearchActions
import dev.matsem.bpm.feature.search.presentation.SearchScreen
import dev.matsem.bpm.feature.search.presentation.SearchState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun SearchScreenUi(
    modifier: Modifier = Modifier,
    screen: SearchScreen = koinInject(),
) {
    val state by screen.state.collectAsStateWithLifecycle()
    SearchScreenUi(state, screen.actions, modifier)
}

@Composable
fun SearchScreenUi(
    state: SearchState,
    actions: SearchActions,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    
    LaunchedEffect(Unit) {
        actions.onSearchInput("")
        focusRequester.requestFocus()
    }

    Column(modifier) {
        DesignTextField(
            value = state.input,
            onValueChanged = actions::onSearchInput,
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .padding(horizontal = BpmTheme.dimensions.horizontalContentPadding),
            placeholder = "Issue key, title, or whatever..."
        )
    }
}

@Preview
@Composable
fun SearchScreenUiPreview() {
    Showcase {
        SearchScreenUi(
            state = SearchState(),
            actions = object : SearchActions {
                override fun onSearchInput(input: String) = Unit
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
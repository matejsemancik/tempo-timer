package dev.matsem.bpm.feature.search.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
package dev.matsem.bpm.feature.search.ui

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.matsem.bpm.data.model.domain.MockIssues
import dev.matsem.bpm.design.input.DesignTextField
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.feature.search.presentation.SearchActions
import dev.matsem.bpm.feature.search.presentation.SearchScreen
import dev.matsem.bpm.feature.search.presentation.SearchState
import dev.matsem.bpm.feature.search.ui.widget.SearchResultRow
import kotlinx.collections.immutable.toImmutableList
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
            placeholder = "Issue key, title, try your luck..."
        )
        VerticalSpacer(Grid.d3)
        Box {
            val listState = rememberLazyListState()
            LazyColumn(
                state = listState
            ) {
                items(state.results) { issue ->
                    SearchResultRow(
                        issue = issue,
                        contentPadding = PaddingValues(horizontal = BpmTheme.dimensions.horizontalContentPadding),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(listState),
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight()
            )
        }
        VerticalSpacer(Grid.d3)
    }
}

@Preview
@Composable
fun SearchScreenUiPreview() {
    Showcase {
        SearchScreenUi(
            state = SearchState(
                results = MockIssues.toImmutableList()
            ),
            actions = SearchActions.noOp(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
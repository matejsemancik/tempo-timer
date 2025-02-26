package dev.matsem.bpm.feature.search.ui

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bpm_tracker.shared.generated.resources.Res
import bpm_tracker.shared.generated.resources.search_placeholder
import dev.matsem.bpm.arch.EventEffect
import dev.matsem.bpm.data.repo.model.Issue
import dev.matsem.bpm.data.repo.model.MockSearchResults
import dev.matsem.bpm.design.input.AppTextField
import dev.matsem.bpm.design.theme.BpmTheme
import dev.matsem.bpm.design.theme.Grid
import dev.matsem.bpm.design.tooling.Showcase
import dev.matsem.bpm.design.tooling.VerticalSpacer
import dev.matsem.bpm.feature.search.presentation.SearchActions
import dev.matsem.bpm.feature.search.presentation.SearchEvent
import dev.matsem.bpm.feature.search.presentation.SearchScreen
import dev.matsem.bpm.feature.search.presentation.SearchState
import dev.matsem.bpm.feature.search.ui.widget.SearchResultRow
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun SearchScreenUi(
    modifier: Modifier = Modifier,
    screen: SearchScreen = koinInject(),
    onIssueSelected: (issue: Issue) -> Unit,
) {
    val state by screen.state.collectAsStateWithLifecycle()
    SearchScreenUi(state, screen.actions, modifier)

    EventEffect(screen.events) { event ->
        when (event) {
            is SearchEvent.IssueSelectedEvent -> onIssueSelected(event.issue)
        }
    }
}

@Composable
fun SearchScreenUi(
    state: SearchState,
    actions: SearchActions,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        actions.onSearchInputSelectAll()
    }

    Column(modifier) {
        AppTextField(
            value = state.input,
            onValueChange = actions::onSearchInput,
            singleLine = true,
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .padding(horizontal = BpmTheme.dimensions.horizontalContentPadding),
            placeholder = stringResource(Res.string.search_placeholder),
            leadingIcon = {
                Icon(Icons.Rounded.Search, contentDescription = null)
            },
            isError = state.textFieldIsError,
            supportingText = state.textFieldSupportingText
        )
        VerticalSpacer(Grid.d3)
        Box {
            val listState = rememberLazyListState()
            LazyColumn(
                state = listState
            ) {
                items(state.results) { searchResult ->
                    SearchResultRow(
                        searchResult = searchResult,
                        contentPadding = PaddingValues(horizontal = BpmTheme.dimensions.horizontalContentPadding),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { actions.onResultClick(searchResult) },
                        onFavouriteClick = { actions.onResultFavouriteClick(searchResult) }
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
                results = MockSearchResults.toImmutableList()
            ),
            actions = SearchActions.noOp(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
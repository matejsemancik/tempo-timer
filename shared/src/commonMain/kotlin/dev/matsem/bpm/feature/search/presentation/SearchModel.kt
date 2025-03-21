package dev.matsem.bpm.feature.search.presentation

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import dev.matsem.bpm.arch.BaseModel
import dev.matsem.bpm.data.repo.IssueRepo
import dev.matsem.bpm.data.repo.model.SearchResult
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException
import kotlin.time.Duration.Companion.milliseconds

private const val SearchInputDebounceMs = 250L

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
internal class SearchModel(
    private val issueRepo: IssueRepo,
) : BaseModel<SearchState, SearchEvent>(SearchState()), SearchScreen {

    override suspend fun onStart() {
        state
            .map { it.input.text }
            .debounce(SearchInputDebounceMs.milliseconds)
            .distinctUntilChanged()
            .filter { it.isNotBlank() }
            .flatMapLatest { query ->
                flow {
                    updateState { it.copy(isLoading = true) }
                    try {
                        issueRepo.searchIssues(query).collect { searchResults ->
                            emit(searchResults)
                        }
                    } catch (cancellation: CancellationException) {
                        return@flow
                    } catch (err: Throwable) {
                        println(err)
                        updateState { it.copy(isLoading = false, error = err) }
                        return@flow
                    }
                }
            }
            .onEach { searchResults ->
                updateState {
                    it.copy(
                        isLoading = false,
                        error = null,
                        didSearch = true,
                        results = searchResults.toImmutableList()
                    )
                }
            }
            .launchIn(coroutineScope)
    }

    override val actions: SearchActions = object : SearchActions {
        override fun onSearchInput(input: TextFieldValue) = updateState { it.copy(input = input) }

        override fun onSearchInputSelectAll() =
            updateState { it.copy(input = it.input.copy(selection = TextRange(0, it.input.text.length))) }

        override fun onResultClick(searchResult: SearchResult) =
            sendEvent(SearchEvent.IssueSelectedEvent(searchResult.issue))

        override fun onResultFavouriteClick(searchResult: SearchResult) {
            coroutineScope.launch {
                if (searchResult.isFavourite) {
                    issueRepo.removeFavouriteIssue(searchResult.issue)
                } else {
                    issueRepo.addFavouriteIssue(searchResult.issue)
                }
            }
        }
    }
}
package dev.matsem.bpm.feature.search.presentation

import dev.matsem.bpm.data.repo.IssueRepo
import dev.matsem.bpm.data.repo.model.SearchResult
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.CancellationException
import kotlin.time.Duration.Companion.milliseconds

private const val SearchInputDebounceMs = 500L

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
internal class SearchModel(
    private val issueRepo: IssueRepo
) : SearchScreen {

    private val coroutineScope = CoroutineScope(Dispatchers.Main) + SupervisorJob()
    private val _state = MutableStateFlow(SearchState())
    override val state: StateFlow<SearchState> = _state

    init {
        state
            .map { it.input }
            .debounce(SearchInputDebounceMs.milliseconds)
            .distinctUntilChanged()
            .filter { it.isNotBlank() }
            .flatMapLatest { query ->
                flow {
                    _state.update { it.copy(isLoading = true) }
                    try {
                        issueRepo.searchIssues(query).collect { searchResults ->
                            emit(searchResults)
                        }
                    } catch (cancellation: CancellationException) {
                        return@flow
                    } catch (err: Throwable) {
                        println(err)
                        _state.update { it.copy(isLoading = false, error = err) }
                        return@flow
                    }
                }
            }
            .onEach { searchResults ->
                _state.update {
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
        override fun onSearchInput(input: String) = _state.update { it.copy(input = input) }
        override fun onResultClick(searchResult: SearchResult) {
            println("Not yet implemented")
        }

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
package dev.matsem.bpm.feature.search.presentation

import dev.matsem.bpm.data.model.domain.SearchResult
import dev.matsem.bpm.data.repo.IssueRepo
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import kotlin.time.Duration.Companion.milliseconds

private const val SearchInputDebounceMs = 500L

internal class SearchModel(
    private val issueRepo: IssueRepo
) : SearchScreen {

    private val coroutineScope = CoroutineScope(Dispatchers.Main) + SupervisorJob()
    private val _state = MutableStateFlow(SearchState())
    override val state: StateFlow<SearchState> = _state

    private val favourites = MutableStateFlow<List<String>>(emptyList())

    init {
        state
            .map { it.input }
            .debounce(SearchInputDebounceMs.milliseconds)
            .distinctUntilChanged()
            .filter { it.isNotBlank() }
            .onEach { query ->
                _state.update { it.copy(isLoading = true) }
                runCatching { issueRepo.searchIssues(query) }
                    .fold(
                        onSuccess = { issues ->
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    didSearch = true,
                                    results = issues.map { issue -> SearchResult(issue, false) }.toPersistentList()
                                )
                            }
                        },
                        onFailure = { err ->
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = err,
                                )
                            }
                        }
                    )

            }
            .launchIn(coroutineScope)
    }

    override val actions: SearchActions = object : SearchActions {
        override fun onSearchInput(input: String) = _state.update { it.copy(input = input) }
        override fun onResultClick(searchResult: SearchResult) {
            TODO("Not yet implemented")
        }

        override fun onResultFavouriteClick(searchResult: SearchResult) {
            TODO("Not yet implemented")
        }
    }
}
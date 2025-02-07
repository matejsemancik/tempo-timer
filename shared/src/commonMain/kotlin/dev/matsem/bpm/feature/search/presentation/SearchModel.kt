package dev.matsem.bpm.feature.search.presentation

import dev.matsem.bpm.data.repo.IssueRepo
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

    init {
        state
            .map { it.input }
            .debounce(SearchInputDebounceMs.milliseconds)
            .distinctUntilChanged()
            .filter { it.isNotBlank() }
            .onEach { query ->
                println("search query: $query")
                val issues = issueRepo.searchIssues(query)
                println(issues.joinToString(separator = "\n") { it.toString() })
            }
            .launchIn(coroutineScope)
    }
    override val actions: SearchActions = object : SearchActions {
        override fun onSearchInput(input: String) = _state.update { it.copy(input = input) }
    }
}
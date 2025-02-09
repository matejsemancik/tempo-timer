package dev.matsem.bpm.feature.search.presentation

import androidx.compose.ui.text.input.TextFieldValue
import dev.matsem.bpm.data.repo.model.SearchResult
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SearchState(
    val input: TextFieldValue = TextFieldValue(),
    val isLoading: Boolean = false,
    val results: ImmutableList<SearchResult> = persistentListOf(),
    internal val error: Throwable? = null,
    val didSearch: Boolean = false,
) {

    val textFieldIsError: Boolean
        get() = error != null

    val textFieldSupportingText: String?
        get() = when {
            error != null -> error.message
            isLoading -> "Loading..."
            didSearch && results.isEmpty() -> "No results"
            else -> null
        }
}
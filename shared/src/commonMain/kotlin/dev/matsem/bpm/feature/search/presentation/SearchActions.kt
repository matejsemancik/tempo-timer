package dev.matsem.bpm.feature.search.presentation

import androidx.compose.ui.text.input.TextFieldValue
import dev.matsem.bpm.data.repo.model.SearchResult

interface SearchActions {
    fun onSearchInput(input: TextFieldValue)

    fun onSearchInputSelectAll()
    fun onResultClick(searchResult: SearchResult)
    fun onResultFavouriteClick(searchResult: SearchResult)

    companion object {
        fun noOp() = object : SearchActions {
            override fun onSearchInput(input: TextFieldValue) = Unit
            override fun onSearchInputSelectAll() = Unit
            override fun onResultClick(searchResult: SearchResult) = Unit
            override fun onResultFavouriteClick(searchResult: SearchResult) = Unit
        }
    }
}
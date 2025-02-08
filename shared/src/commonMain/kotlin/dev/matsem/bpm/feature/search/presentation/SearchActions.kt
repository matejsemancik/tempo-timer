package dev.matsem.bpm.feature.search.presentation

import dev.matsem.bpm.data.repo.model.SearchResult

interface SearchActions {
    fun onSearchInput(input: String)
    fun onResultClick(searchResult: SearchResult)
    fun onResultFavouriteClick(searchResult: SearchResult)

    companion object {
        fun noOp() = object : SearchActions {
            override fun onSearchInput(input: String) = Unit
            override fun onResultClick(searchResult: SearchResult) = Unit
            override fun onResultFavouriteClick(searchResult: SearchResult) = Unit
        }
    }
}
package dev.matsem.bpm.feature.search.presentation

interface SearchActions {
    fun onSearchInput(input: String)

    companion object {
        fun noOp() = object : SearchActions {
            override fun onSearchInput(input: String) = Unit
        }
    }
}
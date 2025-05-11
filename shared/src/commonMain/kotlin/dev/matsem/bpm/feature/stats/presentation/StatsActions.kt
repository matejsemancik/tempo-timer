package dev.matsem.bpm.feature.stats.presentation

interface StatsActions {

    fun onViewResumed()

    companion object {
        fun noOp() = object : StatsActions {
            override fun onViewResumed() = Unit
        }
    }
}

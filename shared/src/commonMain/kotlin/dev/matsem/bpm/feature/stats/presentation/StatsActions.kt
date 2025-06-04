package dev.matsem.bpm.feature.stats.presentation

interface StatsActions {

    fun onViewResumed()
    fun onClick()

    companion object {
        fun noOp() = object : StatsActions {
            override fun onViewResumed() = Unit
            override fun onClick() = Unit
        }
    }
}

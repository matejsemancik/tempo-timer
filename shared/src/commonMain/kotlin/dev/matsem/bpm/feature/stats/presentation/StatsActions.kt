package dev.matsem.bpm.feature.stats.presentation

interface StatsActions {

    companion object {
        fun noOp() = object : StatsActions {}
    }
}

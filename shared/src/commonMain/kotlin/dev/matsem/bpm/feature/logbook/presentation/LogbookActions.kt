package dev.matsem.bpm.feature.logbook.presentation

interface LogbookActions {
    // Add action methods as needed for the logbook feature

    companion object {
        fun noOp() = object : LogbookActions {
            // Implement action methods as needed
        }
    }
}

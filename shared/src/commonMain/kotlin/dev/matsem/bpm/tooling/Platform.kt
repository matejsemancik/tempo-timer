package dev.matsem.bpm.tooling

interface Platform {
    fun getVersionString(): String
    fun isMacos(): Boolean
}

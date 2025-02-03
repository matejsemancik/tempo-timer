package dev.matsem.bpm.data.repo

interface ClearableRepo {
    suspend fun clear()
}
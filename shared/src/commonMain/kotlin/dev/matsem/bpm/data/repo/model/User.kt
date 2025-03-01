package dev.matsem.bpm.data.repo.model

data class User(
    val accountId: String,
    val email: String,
    val displayName: String,
    val avatarUrl: String,
)
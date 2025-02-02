package dev.matsem.bpm.data.service

import de.jensklingenberg.ktorfit.http.GET
import dev.matsem.bpm.data.model.network.jira.User

interface JiraApi {

    @GET("myself")
    suspend fun getMyself(): User
}
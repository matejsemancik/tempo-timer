package dev.matsem.bpm.data.service

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import dev.matsem.bpm.data.model.network.jira.issuePicker.IssuePickerResponse
import dev.matsem.bpm.data.model.network.jira.user.JiraUser

interface JiraApi {

    @GET("myself")
    suspend fun getMyself(): JiraUser

    @GET("issue/picker")
    suspend fun searchIssues(
        @Query("query") query: String,
        @Query("currentJQL") currentJql: String,
        @Query("showSubTasks") showSubTasks: Boolean,
        @Query("showSubTaskParents") showSubTaskParents: Boolean
    ): IssuePickerResponse
}
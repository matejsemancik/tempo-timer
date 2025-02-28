package dev.matsem.bpm.data.service.jira

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import dev.matsem.bpm.data.service.jira.model.issuePicker.IssuePickerResponse
import dev.matsem.bpm.data.service.jira.model.user.JiraUser

interface JiraApi {

    @GET("rest/api/3/myself")
    suspend fun getMyself(): JiraUser

    @GET("rest/api/3/issue/picker")
    suspend fun searchIssues(
        @Query("query") query: String,
        @Query("currentJQL") currentJql: String,
        @Query("showSubTasks") showSubTasks: Boolean,
        @Query("showSubTaskParents") showSubTaskParents: Boolean,
    ): IssuePickerResponse
}
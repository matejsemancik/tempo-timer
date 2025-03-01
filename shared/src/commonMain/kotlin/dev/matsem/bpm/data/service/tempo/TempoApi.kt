package dev.matsem.bpm.data.service.tempo

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import de.jensklingenberg.ktorfit.http.Url
import dev.matsem.bpm.data.service.tempo.model.CreateWorklogBody
import dev.matsem.bpm.data.service.tempo.model.PageableTempoResponse
import dev.matsem.bpm.data.service.tempo.model.Worklog
import kotlinx.datetime.LocalDate

interface TempoApi {

    @GET("worklogs/user/{jiraAccountId}")
    suspend fun getWorklogs(
        @Path("jiraAccountId") jiraAccountId: String,
        @Query("from") from: LocalDate,
        @Query("to") to: LocalDate,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): PageableTempoResponse<Worklog>

    @GET("")
    suspend fun getWorklogs(
        @Url url: String,
    ): PageableTempoResponse<Worklog>

    @POST("worklogs")
    suspend fun createWorklog(@Body body: CreateWorklogBody): Worklog
}
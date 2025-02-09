package dev.matsem.bpm.data.database.dao

import androidx.room.*
import dev.matsem.bpm.data.database.model.FavouriteIssue
import dev.matsem.bpm.data.database.model.JiraIssue
import kotlinx.coroutines.flow.Flow

@Dao
interface JiraIssueDao {

    @Upsert
    suspend fun upsertIssue(issue: JiraIssue)

    @Insert
    suspend fun insertFavourite(favourite: FavouriteIssue)

    @Transaction
    suspend fun addFavouriteIssue(jiraIssue: JiraIssue, favourite: FavouriteIssue) {
        upsertIssue(jiraIssue)
        insertFavourite(favourite)
    }

    @Query("DELETE FROM favourite_issue WHERE favourite_issue.jira_issue_id = :jiraIssueId")
    suspend fun removeFavouriteIssue(jiraIssueId: Long)

    @Query("SELECT * FROM jira_issue INNER JOIN favourite_issue ON favourite_issue.jira_issue_id = jira_issue.id")
    fun getFavouriteIssues(): Flow<List<JiraIssue>>
}
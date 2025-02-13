package dev.matsem.bpm.data.repo

import dev.matsem.bpm.data.database.dao.JiraIssueDao
import dev.matsem.bpm.data.database.model.FavouriteIssue
import dev.matsem.bpm.data.mapping.CredentialsMapping.toDomainModel
import dev.matsem.bpm.data.mapping.IssueMapping.toDbModel
import dev.matsem.bpm.data.mapping.IssueMapping.toDomainModel
import dev.matsem.bpm.data.persistence.ApplicationPersistence
import dev.matsem.bpm.data.repo.model.Issue
import dev.matsem.bpm.data.repo.model.SearchResult
import dev.matsem.bpm.data.service.jira.JiraApiManager
import kotlinx.coroutines.flow.*

interface IssueRepo {

    fun searchIssues(query: String): Flow<List<SearchResult>>

    suspend fun addFavouriteIssue(issue: Issue)

    suspend fun removeFavouriteIssue(issue: Issue)

    fun getFavouriteIssues(): Flow<List<Issue>>
}

internal class IssueRepoImpl(
    private val jiraApiManager: JiraApiManager,
    private val applicationPersistence: ApplicationPersistence,
    private val jiraIssueDao: JiraIssueDao,
) : IssueRepo {
    override fun searchIssues(query: String): Flow<List<SearchResult>> = flow {
        val response = jiraApiManager.searchIssues(
            query = query,
            currentJql = "issue = \"$query\" OR issue ~ \"$query\" OR text ~ \"$query*\"",
            showSubTasks = true,
            showSubTaskParents = true
        )

        val baseApiUrl = applicationPersistence.getCredentials()?.toDomainModel()?.jiraApiUrl
            ?: error("Credentials are not present")

        val issues = response.sections
            .flatMap { section -> section.issues }
            .map { issue -> issue.toDomainModel(baseApiUrl) }
            .distinctBy { issue -> issue.key }
            .map { SearchResult(it, false) }

        emit(issues)
    }.combine(jiraIssueDao.getFavouriteIssues()) { searchResults, persistedFavourites ->
        searchResults.map { searchResult -> searchResult.copy(isFavourite = persistedFavourites.any { fav -> fav.id == searchResult.issue.id }) }
    }

    override suspend fun addFavouriteIssue(issue: Issue) = jiraIssueDao.addFavouriteIssue(
        jiraIssue = issue.toDbModel(),
        favourite = FavouriteIssue(jiraIssueId = issue.id)
    )

    override suspend fun removeFavouriteIssue(issue: Issue) =
        jiraIssueDao.removeFavouriteIssue(jiraIssueId = issue.id)

    override fun getFavouriteIssues(): Flow<List<Issue>> =
        jiraIssueDao.getFavouriteIssues().map { issueList -> issueList.map { issue -> issue.toDomainModel() } }
}
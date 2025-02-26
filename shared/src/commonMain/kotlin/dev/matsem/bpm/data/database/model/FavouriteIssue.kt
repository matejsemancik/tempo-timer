package dev.matsem.bpm.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favourite_issue",
    foreignKeys = [
        ForeignKey(
            entity = JiraIssue::class,
            parentColumns = ["id"],
            childColumns = ["jira_issue_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("jira_issue_id")
    ]
)
data class FavouriteIssue(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "jira_issue_id")
    val jiraIssueId: Long,
)
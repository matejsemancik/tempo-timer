package dev.matsem.bpm.data.database.model

import androidx.room.*
import kotlinx.datetime.Instant

@Entity(
    tableName = "timer",
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
data class Timer(

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "jira_issue_id")
    val jiraIssueId: Long,

    @ColumnInfo(name = "accumulation")
    val accumulationMs: Long,

    @ColumnInfo(name = "started_at")
    val startedAt: Instant?,
)

data class TimerComplete(

    @Embedded val timer: Timer,

    @Relation(
        parentColumn = "jira_issue_id",
        entityColumn = "id"
    )
    val issue: JiraIssue
)
package dev.matsem.bpm.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jira_issue")
data class JiraIssue(

    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "key")
    val key: String,

    @ColumnInfo(name = "summary")
    val sumary: String,

    @ColumnInfo(name = "icon_url")
    val iconUrl: String,
)
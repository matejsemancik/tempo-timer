package dev.matsem.bpm.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(tableName = "timer")
data class Timer(

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "issue_id")
    val issueId: Long,

    @ColumnInfo(name = "started_at")
    val startedAt: Instant?,

    @ColumnInfo(name = "accumulation")
    val accumulationMs: Long
)
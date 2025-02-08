package dev.matsem.bpm.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "issue")
data class Issue(

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
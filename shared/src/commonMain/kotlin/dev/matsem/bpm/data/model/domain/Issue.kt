package dev.matsem.bpm.data.model.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Issue constructor(
    val type: IssueType,
    val key: String,
    val title: String,
)

enum class IssueType(val color: Color, val icon: ImageVector) {
    Bug(color = Color(0xffE5493A), icon = Icons.Filled.FiberManualRecord),
    Task(color = Color(0xff4BADE8), icon = Icons.Filled.CheckCircle),
    Subtask(color = Color(0xff4BADE8), icon = Icons.Filled.CheckCircleOutline),
    Story(color = Color(0xff63BA3C), icon = Icons.Filled.Bookmark),
    Epic(color = Color(0xff904EE2), icon = Icons.Filled.Bolt),
}

val MockIssues = listOf(
    Issue(
        type = IssueType.Bug,
        key = "MTSM-1",
        title = "Dojebaný payment button",
    ),
    Issue(
        type = IssueType.Task,
        key = "MTSM-4",
        title = "Spraviť robotu",
    ),
    Issue(
        type = IssueType.Subtask,
        key = "MTSM-19",
        title = "[AN] Spraviť robotu",
    ),
    Issue(
        type = IssueType.Story,
        key = "MTSM-140",
        title = "Robenie roboty",
    ),
    Issue(
        type = IssueType.Epic,
        key = "MTSM-5",
        title = "Tempo Desktop",
    )
)

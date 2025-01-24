package dev.matsem.bpm.tracker.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class IssueType(val color: Color, val icon: ImageVector) {
    Bug(color = Color(0xffE5493A), icon = Icons.Filled.FiberManualRecord),
    Task(color = Color(0xff4BADE8), icon = Icons.Filled.CheckCircle),
    Subtask(color = Color(0xff4BADE8), icon = Icons.Filled.CheckCircleOutline),
    Story(color = Color(0xff63BA3C), icon = Icons.Filled.Bookmark),
    Epic(color = Color(0xff904EE2), icon = Icons.Filled.Bolt),
}
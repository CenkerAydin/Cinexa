package com.cenkeraydin.composemovie.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cenkeraydin.composemovie.util.ViewMode

@Composable
fun ViewModeToggle(
    viewMode: ViewMode,
    onViewModeChange: (ViewMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.End
    ) {
        // List View
        IconButton(
            onClick = { onViewModeChange(ViewMode.LIST) },
            modifier = Modifier

        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = "List View",
                tint = if (viewMode == ViewMode.LIST) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }

        // Grid 2 Columns
        IconButton(
            onClick = { onViewModeChange(ViewMode.GRID_2) },
            modifier = Modifier

        ) {
            Icon(
                imageVector = Icons.Default.GridView, // ya da başka bir ikon seç
                contentDescription = "Grid 2 View",
                tint = if (viewMode == ViewMode.GRID_2) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }

        // Grid 3 Columns
        IconButton(
            onClick = { onViewModeChange(ViewMode.GRID_3) },
            modifier = Modifier

        ) {
            Icon(
                imageVector = Icons.Default.ViewModule, // farklı bir ikon önerisi
                contentDescription = "Grid 3 View",
                tint = if (viewMode == ViewMode.GRID_3) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }
    }
}


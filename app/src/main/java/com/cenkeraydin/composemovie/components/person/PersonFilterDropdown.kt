package com.cenkeraydin.composemovie.components.person

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cenkeraydin.composemovie.util.MovieFilter
import com.cenkeraydin.composemovie.util.PersonFilter

@Composable
fun PersonFilterDropdown(
    selectedFilter: PersonFilter,
    onFilterSelected: (PersonFilter) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        FilterChip(
            selected = true,
            onClick = { expanded = true },
            label = {
                val title = stringResource(id = selectedFilter.displayName)

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title)
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown icon",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        )


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            PersonFilter.entries.forEach { filter ->
                DropdownMenuItem(
                    text = { Text(stringResource(id = filter.displayName)) },
                    onClick = {
                        onFilterSelected(filter)
                        expanded = false
                    }
                )
            }
        }
    }
}

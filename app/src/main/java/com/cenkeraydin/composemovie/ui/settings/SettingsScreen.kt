package com.cenkeraydin.composemovie.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.cenkeraydin.composemovie.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(id = R.string.settings_title))
                    }
                },
            )
        }
    ) { innerPadding ->
        if (isLandscape) {
            // 🔹 Landscape görünüm
            Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                DarkModeCard(isDarkMode, onDarkModeToggle, Modifier.weight(1f))
                LanguageCard(selectedLanguage, onLanguageSelected, Modifier.weight(1f))
            }
        } else {
            // 🔹 Portrait görünüm
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DarkModeCard(isDarkMode, onDarkModeToggle)
                LanguageCard(selectedLanguage, onLanguageSelected)
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.movie_icon),
                        contentDescription = "Movie Icon",
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DarkModeCard(
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(id = R.string.dark_mode), style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = isDarkMode,
                onCheckedChange = onDarkModeToggle
            )
        }
    }
}

@Composable
private fun LanguageCard(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
                .verticalScroll(rememberScrollState())

        ) {
            Text(
                text = stringResource(id = R.string.language_selection),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LanguageDropdown(
                selectedLanguage = selectedLanguage,
                onLanguageSelected = onLanguageSelected
            )
        }
    }
}


@Composable
fun LanguageDropdown(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val languages = listOf(
        Triple(stringResource(R.string.english), R.drawable.uk_flag, "en"),
        Triple(stringResource(R.string.turkish), R.drawable.turkish_flag, "tr"),
        Triple(stringResource(R.string.spanish), R.drawable.spanish_flag, "es"),
        Triple(stringResource(R.string.german), R.drawable.german_flag, "de")
    )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(6.dp)
                )
                .clickable { expanded = !expanded }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val selected = languages.find { it.first == selectedLanguage }
            Row(verticalAlignment = Alignment.CenterVertically) {
                selected?.let {
                    Image(
                        painter = painterResource(id = it.second),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(it.first)
                }
            }
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                languages.forEach { (name, flag, code) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (name == selectedLanguage)
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                else
                                    Color.Transparent
                            )
                            .clickable {
                                onLanguageSelected(name)
                                expanded = false
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = flag),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(name)
                    }
                }
            }
        }
    }
}

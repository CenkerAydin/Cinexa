package com.cenkeraydin.composemovie.ui.main

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cenkeraydin.composemovie.AppNavigation
import com.cenkeraydin.composemovie.ui.bar.BottomBar
import com.cenkeraydin.composemovie.ui.bar.BottomNavItem

@Composable
fun MainScreen() {
    val systemInDarkTheme = isSystemInDarkTheme()
    val context = LocalContext.current

    val savedDarkMode = remember {
        val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        prefs.getBoolean("dark_mode", systemInDarkTheme)
    }

    var isDarkMode by remember { mutableStateOf(savedDarkMode) }

    MyTheme(darkTheme = isDarkMode) {
        val navController = rememberNavController()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val showBottomBar = currentRoute in listOf(
            "movie_detail/{movieId}",
            "person_detail/{personId}",
            "serie_detail/{serieId}",
            "person_detail_screen/{personId}",
            BottomNavItem.Movie.route,
            BottomNavItem.Series.route,
            BottomNavItem.Favorites.route,
            BottomNavItem.Settings.route,
            BottomNavItem.Person.route
        )

        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    BottomBar(navController)
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                AppNavigation(
                    navController = navController,
                    isDarkMode = isDarkMode,
                    onDarkModeToggle = { newDarkMode ->
                        isDarkMode = newDarkMode
                        val prefs =
                            context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
                        prefs.edit().putBoolean("dark_mode", newDarkMode).apply()
                    }
                )
            }
        }
    }
}

@Composable
fun MyTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme(),
        content = content
    )
}
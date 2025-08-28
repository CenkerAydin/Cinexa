package com.cenkeraydin.composemovie

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cenkeraydin.composemovie.ui.bar.BottomNavItem
import com.cenkeraydin.composemovie.ui.favorite.FavoritesScreen
import com.cenkeraydin.composemovie.ui.movie_detail.DetailScreen
import com.cenkeraydin.composemovie.ui.movie.MovieScreen
import com.cenkeraydin.composemovie.ui.person.PersonScreen
import com.cenkeraydin.composemovie.ui.person_detail.PersonDetailScreen
import com.cenkeraydin.composemovie.ui.person_movie.PersonMovieScreen
import com.cenkeraydin.composemovie.ui.person_serie.PersonSerieScreen
import com.cenkeraydin.composemovie.ui.serie_detail.SerieDetailScreen
import com.cenkeraydin.composemovie.ui.series.SeriesScreen
import com.cenkeraydin.composemovie.ui.settings.SettingsScreen
import com.cenkeraydin.composemovie.util.setLocale



@Composable
fun AppNavigation(
    navController: NavHostController,
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit) {

    val context = LocalContext.current
    val systemLanguage = remember {
        when (context.resources.configuration.locales[0].language) {
            "tr" -> "Türkçe"
            "es" -> "Español"
            "de" -> "Deutsch"
            else -> "English"
        }
    }

    var selectedLanguage by remember { mutableStateOf(systemLanguage) }


    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Movie.route
    ) {
        composable(BottomNavItem.Movie.route) {
            MovieScreen(navController)
        }
        composable(BottomNavItem.Series.route) {
            SeriesScreen(navController)
        }
        composable(BottomNavItem.Favorites.route) {
            FavoritesScreen(navController)
        }
        composable(BottomNavItem.Person.route) {
            PersonScreen(navController)
        }
        composable(BottomNavItem.Settings.route) {

            val turkish = stringResource(R.string.turkish)
            val spanish = stringResource(R.string.spanish)
            val german  = stringResource(R.string.german)
            val english = stringResource(R.string.english)

            val langToCode = mapOf(
                turkish to "tr",
                spanish to "es",
                german  to "de",
                english to "en"
            )
            SettingsScreen(
                isDarkMode = isDarkMode,
                onDarkModeToggle = onDarkModeToggle,
                selectedLanguage = selectedLanguage,
                onLanguageSelected = { languageDisplayName ->
                    val code = langToCode[languageDisplayName] ?: "en"
                    context.setLocale(code)
                    (context as? Activity)?.recreate()
                }
            )
        }
        composable(
            route = "movie_detail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            DetailScreen(movieId = movieId, navController)
        }
        composable(
            route = "person_detail/{personId}",
            arguments = listOf(navArgument("personId") { type = NavType.IntType })
        ) { backStackEntry ->
            val personId = backStackEntry.arguments?.getInt("personId") ?: 0
            PersonMovieScreen(navController, personId)
        }
        composable(
            "person_detail_screen/{personId}",
            arguments = listOf(navArgument("personId") { type = NavType.IntType })
        ) { backStackEntry ->
            val personId = backStackEntry.arguments?.getInt("personId") ?: 0
            PersonDetailScreen( personId,navController)
        }
        composable(
            route = "serie_detail/{serieId}",
            arguments = listOf(navArgument("serieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val serieId = backStackEntry.arguments?.getInt("serieId") ?: 0
            SerieDetailScreen(
                serieId = serieId,
                navHostController = navController
            )
        }
        composable(
            route = "person_serie_detail/{personId}",
            arguments = listOf(navArgument("personId") { type = NavType.IntType })
        ) { backStackEntry ->
            val personId = backStackEntry.arguments?.getInt("personId") ?: 0
            PersonSerieScreen(navController, personId)
        }

    }
}

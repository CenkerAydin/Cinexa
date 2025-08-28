package com.cenkeraydin.composemovie.ui.bar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.cenkeraydin.composemovie.R

sealed class BottomNavItem(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val titleRes: Int
) {
    object Movie : BottomNavItem("movie", R.drawable.round_movie_24, R.string.movies_title )
    object Series : BottomNavItem("series", R.drawable.baseline_local_movies_24, R.string.series_title)
    object Favorites : BottomNavItem("favorites", R.drawable.baseline_favorite_24, R.string.favorites_title)
    object Settings : BottomNavItem("settings", R.drawable.baseline_settings_24, R.string.settings_title)
    object Person: BottomNavItem("person", R.drawable.baseline_person_24, R.string.person_title)
}

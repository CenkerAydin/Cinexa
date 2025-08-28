package com.cenkeraydin.composemovie.ui.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cenkeraydin.composemovie.R
import com.cenkeraydin.composemovie.components.movie.FavoriteCardGrid
import com.cenkeraydin.composemovie.util.FavoriteCategory
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration

@Composable
fun FavoritesScreen(
    navHostController: NavHostController,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val columns = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2

    var selectedCategory by remember { mutableStateOf(FavoriteCategory.MOVIES) }
    val categories = listOf(
        FavoriteCategory.MOVIES to stringResource(R.string.movies_title),
        FavoriteCategory.SERIES to stringResource(R.string.series_title),
        FavoriteCategory.PERSONS to stringResource(R.string.person_title)
    )
    val movieFavorites by viewModel.movieFavorites.collectAsState()
    val serieFavorites by viewModel.serieFavorites.collectAsState()
    val personFavorites by viewModel.personFavorites.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllFavorites()

    }
    LaunchedEffect(Unit) {
        viewModel.getAllSerieFavorites()
    }
    LaunchedEffect(Unit) {
        viewModel.getAllPersonFavorites()
    }

    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.favorites),
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center
                    )
                }

                // Kategori seçici bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    categories.forEach { (categoryKey, categoryName) ->
                        val isSelected = categoryKey == selectedCategory
                        TextButton(
                            onClick = { selectedCategory = categoryKey },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = if (isSelected) Color.Black else Color.Gray,
                                containerColor = if (isSelected) Color(0xFFff6b35) else Color.Transparent
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(text = categoryName)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedCategory) {
                FavoriteCategory.MOVIES -> {
                    if (movieFavorites.isEmpty()) {
                        EmptyState(
                            iconRes = R.drawable.movie,
                            message = stringResource(R.string.no_favorite_film)
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(columns),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(movieFavorites) { _, movie ->
                                FavoriteCardGrid(
                                    title = movie.title,
                                    rating = movie.voteAverage,
                                    imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                                    isLandscape = isLandscape,
                                    onClick = {
                                        navHostController.navigate("movie_detail/${movie.id}")
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                FavoriteCategory.SERIES -> {
                    if (serieFavorites.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.movie),
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.no_favorite_serie),
                                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = Color.Gray
                                )
                            }
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(columns),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(serieFavorites) { _, serie ->
                                FavoriteCardGrid(
                                    title = serie.title,
                                    rating = serie.voteAverage,
                                    imageUrl = "https://image.tmdb.org/t/p/w500${serie.posterPath}",
                                    isLandscape = isLandscape,
                                    onClick = {
                                        navHostController.navigate("serie_detail/${serie.id}")
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                FavoriteCategory.PERSONS -> {
                    if (personFavorites.isEmpty()) {
                        EmptyState(
                            iconRes = R.drawable.baseline_person_24,
                            message = stringResource(R.string.no_favorite_person)
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(columns),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(personFavorites) { _, person ->
                                FavoriteCardGrid(
                                    title = person.name ?: "Bilinmeyen",
                                    rating = person.popularity,
                                    imageUrl = "https://image.tmdb.org/t/p/w500${person.profilePath}",
                                    isLandscape = isLandscape,
                                    onClick = {
                                        navHostController.navigate("person_detail_screen/${person.id}")
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState(iconRes: Int, message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}

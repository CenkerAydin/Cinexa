package com.cenkeraydin.composemovie.ui.movie

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cenkeraydin.composemovie.R
import com.cenkeraydin.composemovie.components.ModernSearchBar
import com.cenkeraydin.composemovie.components.movie.MovieCard
import com.cenkeraydin.composemovie.components.movie.MovieCardGrid
import com.cenkeraydin.composemovie.components.ViewModeToggle
import com.cenkeraydin.composemovie.components.movie.MoviesFilterDropdown
import com.cenkeraydin.composemovie.data.model.movie.Movie
import com.cenkeraydin.composemovie.util.MovieFilter
import com.cenkeraydin.composemovie.util.UIState
import com.cenkeraydin.composemovie.util.ViewMode

@Composable
fun MovieScreen(
    navHostController: NavHostController,
    viewModel: MovieViewModel = hiltViewModel()
) {

    val movieState by viewModel.movies.collectAsState()

    val genreMap by viewModel.genres.collectAsState()
    var viewMode by rememberSaveable { mutableStateOf(ViewMode.LIST) }
    val selectedGenreId by viewModel.selectedGenreId.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.movieFilter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem != null && lastVisibleItem.index >= totalItems - 3
        }
    }
    val shouldLoadMoreGrid = remember {
        derivedStateOf {
            val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = gridState.layoutInfo.totalItemsCount
            lastVisibleItem != null && lastVisibleItem.index >= totalItems - 5
        }
    }

    LaunchedEffect(selectedGenreId, selectedFilter, searchQuery) {
        listState.scrollToItem(0)
        gridState.scrollToItem(0)
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && !isLoading) {
            viewModel.fetchNextPageIfNeeded()
        }
    }
    LaunchedEffect(shouldLoadMoreGrid.value) {
        if (shouldLoadMoreGrid.value && !isLoading) {
            viewModel.fetchNextPageIfNeeded()
        }
    }
    Column(modifier = Modifier.padding(top = if (isLandscape) 8.dp else 32.dp)) {

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Search
                ModernSearchBar(
                    query = searchQuery,
                    placeholder = stringResource(R.string.search_movies),
                    onQueryChange = { viewModel.searchMovies(it) },
                    onSearch = { viewModel.searchMovies(it) },
                    modifier = Modifier.weight(1f)
                )

                // Filtre dropdown
                MoviesFilterDropdown(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { viewModel.setMovieFilter(it) }
                )
            }


        } else {
            // 📱 Dikey görünüm (senin mevcut kodun)
            ModernSearchBar(
                query = searchQuery,
                placeholder = stringResource(R.string.search_movies),
                onQueryChange = { viewModel.searchMovies(it) },
                onSearch = { viewModel.searchMovies(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedGenreId == null,
                        onClick = { viewModel.selectGenre(null) },
                        label = { Text(stringResource(R.string.all)) }
                    )
                }

                genreMap.forEach { (id, name) ->
                    item {
                        FilterChip(
                            selected = selectedGenreId == id,
                            onClick = { viewModel.selectGenre(id) },
                            label = { Text(name) }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MoviesFilterDropdown(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { viewModel.setMovieFilter(it) }
                )

                ViewModeToggle(viewMode = viewMode) {
                    viewMode = it
                }

            }
        }




        Column(modifier = Modifier.padding(8.dp)) {
            when (movieState) {
                is UIState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UIState.Error -> {
                    val error = (movieState as UIState.Error).message
                    val errorMessage = when {
                        error.contains("Unable to resolve host", ignoreCase = true) -> stringResource(R.string.check_internet)
                        else -> stringResource(R.string.no_film)
                    }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = errorMessage,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is UIState.Success -> {
                    val movies = (movieState as UIState.Success<List<Movie>>).data
                    if (movies.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_film),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {


                        when (viewMode) {

                            ViewMode.LIST -> {
                                if (movies.isEmpty()) {
                                    if (isLoading) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(top = 32.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = stringResource(R.string.no_film),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color.Gray,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                    return@Column
                                }
                                if (isLoading) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                } else {
                                    LazyColumn(
                                        state = listState
                                    ) {

                                        itemsIndexed(
                                            movies,
                                            key = { index, movie -> "${movie.id}_$index" } )
                                            { index, movie ->
                                            val isFavorite by viewModel.isFavorite(movie.id)
                                                .collectAsState(initial = false)

                                            MovieCard(
                                                title = movie.title,
                                                year = movie.releaseDate,
                                                genre = movie.genreIds.firstOrNull()
                                                    ?.let { genreMap[it] }
                                                    ?: "Unknown",
                                                rating = movie.voteAverage,
                                                imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                                                movieId = movie.id,
                                                isFavorite = isFavorite,
                                                onFavoriteClick = { viewModel.toggleFavorite(movie) },
                                                onClick = {
                                                    navHostController.navigate("movie_detail/${movie.id}")
                                                },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            if (index >= movies.size - 3) {
                                                when (selectedFilter) {
                                                    MovieFilter.TRENDING -> viewModel.fetchTrendingMovies()
                                                    MovieFilter.POPULAR -> viewModel.fetchPopularMovies()
                                                    MovieFilter.TOP_RATED -> viewModel.fetchTopRatedMovies()
                                                }
                                            }
                                        }

                                    }

                                }

                            }

                            ViewMode.GRID_2, ViewMode.GRID_3 -> {
                                val columns = if (viewMode == ViewMode.GRID_2) 2 else 3
                                if (movies.isEmpty()) {
                                    if (isLoading) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(top = 32.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = stringResource(R.string.no_film),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color.Gray,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                    return@Column
                                }
                                if (isLoading) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                } else {
                                    LazyVerticalGrid(
                                        state = gridState,
                                        columns = GridCells.Fixed(columns),
                                        modifier = Modifier.fillMaxSize(),
                                        contentPadding = PaddingValues(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        itemsIndexed(
                                            movies,
                                            key = { index, movie -> "${movie.id}_$index" } )
                                        { index, movie ->
                                            val isFavorite by viewModel.isFavorite(movie.id)
                                                .collectAsState(initial = false)

                                            MovieCardGrid(
                                                title = movie.title,
                                                rating = movie.voteAverage,
                                                imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                                                isLiked = isFavorite,
                                                onFavoriteClick = { viewModel.toggleFavorite(movie) },
                                                onClick = {
                                                    navHostController.navigate("movie_detail/${movie.id}")
                                                },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            if (index >= movies.size - 5) {
                                                when (selectedFilter) {
                                                    MovieFilter.TRENDING -> viewModel.fetchTrendingMovies()
                                                    MovieFilter.POPULAR -> viewModel.fetchPopularMovies()
                                                    MovieFilter.TOP_RATED -> viewModel.fetchTopRatedMovies()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
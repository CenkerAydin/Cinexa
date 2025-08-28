package com.cenkeraydin.composemovie.ui.series

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.navigation.NavController
import com.cenkeraydin.composemovie.R
import com.cenkeraydin.composemovie.components.ModernSearchBar
import com.cenkeraydin.composemovie.components.serie.SeriesFilterDropdown
import com.cenkeraydin.composemovie.components.serie.SeriesCard
import com.cenkeraydin.composemovie.components.serie.SeriesCardGrid
import com.cenkeraydin.composemovie.components.ViewModeToggle
import com.cenkeraydin.composemovie.util.ViewMode

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SeriesScreen(
    navController: NavController,
    viewModel: SeriesViewModel = hiltViewModel()
) {
    val series by viewModel.series.collectAsState()
    var viewMode by rememberSaveable { mutableStateOf(ViewMode.LIST) }
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.seriesFilter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val genreMap by viewModel.genres.collectAsState()
    val selectedGenreId by viewModel.selectedGenreId.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


    LaunchedEffect(searchQuery) {
        if (searchQuery.isBlank() && series.isEmpty()) {
            viewModel.fetchPopularSeries()
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
                ModernSearchBar(
                    query = searchQuery,
                    placeholder = stringResource(R.string.search_tv_series),
                    onQueryChange = { newQuery ->
                        viewModel.searchTV(newQuery)
                    },
                    onSearch = {
                        viewModel.searchTV(it)
                    },
                    modifier = Modifier.weight(1f)
                )
                SeriesFilterDropdown(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { viewModel.setSeriesFilter(it) }
                )
            }
        }else{

            ModernSearchBar(
                query = searchQuery,
                placeholder = stringResource(R.string.search_tv_series),
                onQueryChange = { newQuery ->
                    viewModel.searchTV(newQuery)
                },
                onSearch = {
                    viewModel.searchTV(it)
                },
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
                        onClick = { viewModel.selectGenreSerie(null) },
                        label = { Text(stringResource(R.string.all)) }
                    )
                }

                genreMap.forEach { (id, name) ->
                    item {
                        FilterChip(
                            selected = selectedGenreId == id,
                            onClick = { viewModel.selectGenreSerie(id) },
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
                SeriesFilterDropdown(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { viewModel.setSeriesFilter(it) }
                )

                ViewModeToggle(viewMode = viewMode) {
                    viewMode = it
                }

            }
        }

        if(series.isEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_serie),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
            return@Column
        }
        if (searchQuery.isNotBlank() && series.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_serie),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
            return@Column
        }



        Column(modifier = Modifier.padding(8.dp)) {

            when (viewMode) {

                ViewMode.LIST -> {
                    if (isLoading && series.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }else {
                        LazyColumn {

                            itemsIndexed(series) { index, serie ->
                                val isFavorite by viewModel.isFavorite(serie.id).collectAsState(initial = false)

                                SeriesCard(
                                    title = serie.name,
                                    rating = serie.voteAverage,
                                    genre = serie.genreIds.firstNotNullOfOrNull { genreMap[it] } ?: "Unknown",
                                    isFavorite = isFavorite,
                                    onFavoriteClick = {
                                        viewModel.toggleFavorite(serie)
                                    },
                                    imageUrl = "https://image.tmdb.org/t/p/w500${serie.posterPath}",
                                    firstAirDate = serie.firstAirDate,
                                    onClick = {
                                        navController.navigate("serie_detail/${serie.id}")
                                    },
                                )
                                if (index >= series.size - 3) {
                                    viewModel.fetchPopularSeries()
                                }

                            }

                        }
                    }

                }

                ViewMode.GRID_2, ViewMode.GRID_3 -> {
                    val columns = if (viewMode == ViewMode.GRID_2) 2 else 3

                    if (isLoading && series.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(columns),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            itemsIndexed(series) { index, serie ->
                                val isFavorite by viewModel.isFavorite(serie.id).collectAsState(initial = false)

                                SeriesCardGrid(
                                    name = serie.name,
                                    rating = serie.voteAverage,
                                    isLiked = isFavorite,
                                    onFavoriteClick = {
                                        viewModel.toggleFavorite(serie)
                                    },
                                    imageUrl = "https://image.tmdb.org/t/p/w500${serie.posterPath}",
                                    onClick = {
                                        navController.navigate("serie_detail/${serie.id}")

                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                if (index >= series.size - 5) {
                                    viewModel.fetchPopularSeries()
                                }
                            }
                        }
                    }
                }
            }
        }
    }



}
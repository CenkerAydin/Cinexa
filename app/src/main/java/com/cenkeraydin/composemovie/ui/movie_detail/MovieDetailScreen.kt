package com.cenkeraydin.composemovie.ui.movie_detail


import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cenkeraydin.composemovie.R
import com.cenkeraydin.composemovie.components.movie.MovieDetailCard
import com.cenkeraydin.composemovie.util.UIState
import com.cenkeraydin.composemovie.util.openYouTube
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    movieId: Int,
    navHostController: NavHostController,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val movieDetailState by viewModel.movieDetail.collectAsState()
    val cast by viewModel.castList.collectAsState()
    val videoState by viewModel.videoState.collectAsState()
    val context = LocalContext.current
    val isFavorite by viewModel.isFavorite(movieId).collectAsState(initial = false)

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE



    LaunchedEffect(Unit) {
        viewModel.getMovieCredits(movieId)
    }

    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetail(movieId)
    }

    LaunchedEffect(videoState) {
        if (videoState is UIState.Error) {
            errorMessage = (videoState as UIState.Error).message
            showErrorDialog = true
        }
    }

    when (movieDetailState) {
        is UIState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UIState.Success -> {
            val movieDetail = (movieDetailState as UIState.Success).data
            val genreList = movieDetail.genres.map { it.name }

            MovieDetailCard(
                movieTitle = movieDetail.title,
                year = movieDetail.releaseDate,
                runtime = movieDetail.runTime,
                genre = genreList,
                castList = cast,
                rating = movieDetail.voteAverage,
                isFavorite = isFavorite,
                description = movieDetail.overview,
                posterPath = movieDetail.posterPath,
                onBackClick = { navHostController.popBackStack() },
                onPlayClick = {
                    viewModel.getMovieVideos(movieId) { videoKey ->
                        openYouTube(context,videoKey)
                    }
                },
                onHeartClick = { viewModel.toggleFavorite(movieDetail) },
                onShareClick = {
                    scope.launch {
                        viewModel.shareMovieWithPoster(context, movieDetail)
                    }
                },
                isLandscape = isLandscape,
                onPersonClick = { personId ->
                    navHostController.navigate("person_detail/$personId")
                }
            )
        }

        is UIState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .background(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = "Hata",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.something_wrong),
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                }
            }
        }

    }

    if (videoState is UIState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(R.string.search_trailer),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.trailer_error),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },

            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text(stringResource(R.string.okey))
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

package com.cenkeraydin.composemovie.ui.serie_detail

import android.content.res.Configuration
import android.util.Log
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
import com.cenkeraydin.composemovie.components.serie.SerieDetailCard
import com.cenkeraydin.composemovie.util.UIState
import com.cenkeraydin.composemovie.util.openYouTube
import kotlinx.coroutines.launch

@Composable
fun SerieDetailScreen(
    serieId: Int,
    navHostController: NavHostController,
    serieDetailViewModel: SerieDetailViewModel = hiltViewModel()
) {
    val serieDetailState by serieDetailViewModel.serieDetail.collectAsState()
    val cast by serieDetailViewModel.castList.collectAsState()
    val isFavorite by serieDetailViewModel.isFavorite(serieId).collectAsState(initial = false)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val serieVideoState by serieDetailViewModel.videoState.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(serieVideoState) {
        if (serieVideoState is UIState.Error) {
            errorMessage = (serieVideoState as UIState.Error).message
            showErrorDialog = true
        }
    }
    LaunchedEffect(serieId) {
        serieDetailViewModel.fetchSerieDetail(serieId)
    }
    LaunchedEffect(Unit) {
        serieDetailViewModel.getSerieCredits(serieId)
    }


    when (serieDetailState) {
        is UIState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UIState.Success -> {
            val serieDetail = (serieDetailState as UIState.Success).data
            SerieDetailCard(
                serieName = serieDetail.name,
                firstAirDate = serieDetail.firstAirDate,
                lastAirDate = serieDetail.lastAirDate,
                posterPath = serieDetail.posterPath,
                rating = serieDetail.voteAverage,
                isFavorite = isFavorite,
                onHeartClick = { serieDetailViewModel.toggleFavorite(serieDetail) },
                castList = cast,
                seasons = serieDetail.numberOfSeasons,
                description = serieDetail.overview,
                onBackClick = { navHostController.popBackStack() },
                onShareClick = {
                    scope.launch {
                        serieDetailViewModel.shareSerieWithPoster(context, serieDetail)
                    }
                },
                isLandscape = isLandscape,
                onPlayClick = {
                    serieDetailViewModel.getSerieVideos(serieId) { videoKey ->
                        openYouTube(context, videoKey)
                    }
                },
                onPersonClick = { personId ->
                    Log.e("SerieDetailScreen", "Person ID: $personId")
                    navHostController.navigate("person_serie_detail/$personId")
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
    if (serieVideoState is UIState.Loading) {
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
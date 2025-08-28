package com.cenkeraydin.composemovie.ui.person_movie

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.cenkeraydin.composemovie.components.movie.PersonMovieDetailCard
import com.cenkeraydin.composemovie.util.UIState

@Composable
fun PersonMovieScreen(
    navHostController: NavHostController,
    personId: Int,
    viewModel: PersonMovieViewModel = hiltViewModel()
) {
    val personState by viewModel.personDetails.collectAsState()
    val movieCredits by viewModel.movieCredits.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(personId) {
        viewModel.fetchPersonDetails(personId)
        viewModel.fetchPersonMovieCredits(personId)
    }

    when (personState) {
        is UIState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Black)
            }
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


        is UIState.Success -> {
            val person = (personState as UIState.Success).data
            PersonMovieDetailCard(
                navHostController,
                name = person.name,
                biography = person.biography ?: "",
                birthday = person.birthday,
                placeOfBirth = person.placeOfBirth,
                profilePath = person.profilePath ?: "",
                movies = movieCredits,
                onClick = { movie ->
                    navHostController.navigate("movie_detail/${movie.id}")
                },
                isLandscape = isLandscape

            )
        }

    }
}


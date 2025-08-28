package com.cenkeraydin.composemovie.components.person

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.cenkeraydin.composemovie.R
import com.cenkeraydin.composemovie.data.model.person.CombinedCreditItem
import java.util.Locale

@Composable
fun PersonDetailCard(
    navHostController: NavHostController,
    name: String?,
    biography: String,
    birthday: String?,
    placeOfBirth: String?,
    profilePath: String,
    knownFor: List<CombinedCreditItem>,
    onBackClick: () -> Unit = {},
    onClick: (CombinedCreditItem) -> Unit = {},
    isLandscape: Boolean = false
) {
    val movies = knownFor.filter { it.media_type == "movie" }
    val series = knownFor.filter { it.media_type == "tv" }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1a1a2e),
                        Color(0xFF16213e),
                        Color(0xFF0f1419)
                    )
                )
            )
    ) {
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Sol panel: profil bilgileri
                Column(
                    modifier = Modifier
                        .width(280.dp)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { onBackClick()},
                        modifier = Modifier
                            .align(Alignment.Start)
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Geri Dön",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileImage(name, profilePath)
                    Spacer(modifier = Modifier.height(20.dp))
                    NameAndBasicInfo(name, birthday, placeOfBirth)
                }

                Spacer(modifier = Modifier.width(24.dp))

                // Sağ panel: biyografi ve filmler + diziler
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    BiographySection(biography)
                    Spacer(modifier = Modifier.height(30.dp))
                    MoviesSection(movies, onClick)
                    Spacer(modifier = Modifier.height(30.dp))
                    SeriesSection(series, onClick)
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        } else {
            // Portrait layout (mevcut düzen)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    IconButton(
                        onClick = { onBackClick()},
                        modifier = Modifier
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Geri Dön",
                            tint = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier.align(Alignment.TopCenter)
                    ) {
                        ProfileImage(name, profilePath)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                NameAndBasicInfo(name, birthday, placeOfBirth)

                Spacer(modifier = Modifier.height(30.dp))

                BiographySection(biography)

                Spacer(modifier = Modifier.height(30.dp))

                MoviesSection(movies, onClick)

                Spacer(modifier = Modifier.height(30.dp))

                SeriesSection(series, onClick)

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}



@Composable
private fun ProfileImage(name: String?, profilePath: String) {
    Card(
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .size(180.dp)
            .clip(CircleShape)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w300$profilePath",
            contentDescription = "Profile Picture of $name",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun NameAndBasicInfo(name: String?, birthday: String?, placeOfBirth: String?) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name ?: "",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            birthday?.let {
                Text(
                    text = stringResource(R.string.birthday, it),
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 16.sp
                )
            }
            placeOfBirth?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.birthday_place, it),
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun BiographySection(biography: String) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = stringResource(R.string.biography),
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        val deviceLangName = Locale.getDefault().getDisplayLanguage(Locale.getDefault())

        Text(
            text = if (biography.isNotBlank()) biography else stringResource(R.string.biography_not_found_lang, deviceLangName),
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun MoviesSection(
    movies: List<CombinedCreditItem>,
    onClick: (CombinedCreditItem) -> Unit
) {
    Text(
        text = stringResource(R.string.movies),
        color = Color.White,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
    Spacer(modifier = Modifier.height(12.dp))

    if (movies.isNotEmpty()) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movies) { item ->
                PersonKnowForItem(item, onClick = { onClick(item) })
            }
        }
    } else {
        Text(
            text = stringResource(R.string.film_not_found),
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Composable
private fun SeriesSection(
    series: List<CombinedCreditItem>,
    onClick: (CombinedCreditItem) -> Unit
) {
    Text(
        text = stringResource(R.string.series),
        color = Color.White,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
    Spacer(modifier = Modifier.height(12.dp))

    if (series.isNotEmpty()) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(series) { item ->
                PersonKnowForItem(item, onClick = { onClick(item) })
            }
        }
    } else {
        Text(
            text = stringResource(R.string.no_series_found),
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

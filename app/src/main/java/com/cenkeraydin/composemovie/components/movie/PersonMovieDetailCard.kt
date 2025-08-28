package com.cenkeraydin.composemovie.components.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ImageNotSupported
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.cenkeraydin.composemovie.R
import com.cenkeraydin.composemovie.data.model.movie.CastMovie
import java.util.Locale


@Composable
fun PersonMovieDetailCard(
    navHostController: NavHostController,
    name: String,
    biography: String,
    birthday: String?,
    placeOfBirth: String?,
    profilePath: String,
    movies: List<CastMovie>,
    onClick: (CastMovie) -> Unit = {},
    isLandscape: Boolean = false
) {
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
            // 🔹 Landscape görünüm
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Sol: Profil fotoğrafı
                Column(
                    modifier = Modifier
                        .width(250.dp)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { navHostController.popBackStack() },
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
                    Spacer(Modifier.height(16.dp))
                    Card(
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(6.dp),
                        modifier = Modifier
                            .size(200.dp)
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

                Spacer(Modifier.width(24.dp))

                // Sağ: Bilgiler, biyografi ve filmler
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(16.dp))

                    birthday?.takeIf { it.isNotBlank() }?.let {
                        Text(
                            text = stringResource(R.string.birthday, it),
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 16.sp
                        )
                    }

                    placeOfBirth?.takeIf { it.isNotBlank() }?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.birthday_place, it),
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 16.sp
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = stringResource(R.string.biography),
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(12.dp))

                    if (biography.isNotBlank()) {
                        Text(
                            text = biography,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    } else {
                        val deviceLangName =
                            Locale.getDefault().getDisplayLanguage(Locale.getDefault())
                        Text(
                            text = stringResource(
                                R.string.biography_not_found_lang,
                                deviceLangName
                            ),
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 14.sp
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = stringResource(R.string.movies),
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(12.dp))

                    if (movies.isNotEmpty()) {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 0.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(movies) { movie ->
                                MovieItem(movie) { onClick(movie) }
                            }
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.film_not_found),
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        } else {
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Profile Image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        IconButton(
                            onClick = { navHostController.popBackStack() },
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(start = 16.dp)
                                .size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Geri Dön",
                                tint = Color.White
                            )
                        }

                        // Profil fotoğrafı ortalanmış
                        Box(
                            modifier = Modifier.align(Alignment.TopCenter)
                        ) {
                            Card(
                                shape = CircleShape,
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
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
                    }



                    Spacer(modifier = Modifier.height(30.dp))

                    // Name
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Birthday and Place of Birth
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        if (!birthday.isNullOrBlank()) {
                            Text(
                                text = stringResource(R.string.birthday, birthday),
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp
                            )
                        }
                        if (!placeOfBirth.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.birthday_place, placeOfBirth),
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // Biography
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            text = stringResource(R.string.biography),
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (biography.isNotBlank()) {
                            Text(
                                text = biography,
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        } else {
                            val deviceLangName =
                                Locale.getDefault().getDisplayLanguage(Locale.getDefault())
                            Text(
                                text = stringResource(
                                    R.string.biography_not_found_lang,
                                    deviceLangName
                                ),
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            text = stringResource(R.string.movies),
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (movies.isNotEmpty()) {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(movies) { movie ->
                                    MovieItem(movie, onClick = { onClick(movie) })
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

                    Spacer(modifier = Modifier.height(30.dp))


                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: CastMovie, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            if (movie.poster_path != null) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w185${movie.poster_path}",
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ImageNotSupported,
                            contentDescription = "No Image",
                            tint = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Image Not Found",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                    }
                }

            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = movie.title,
            color = Color.White,
            fontSize = 12.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        movie.release_date?.let {
            Text(
                text = "(${it.take(4)})",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 10.sp
            )
        }
    }
}



package com.cenkeraydin.composemovie.components.movie

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cenkeraydin.composemovie.R
import com.cenkeraydin.composemovie.data.model.Cast
import kotlin.math.floor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailCard(
    movieTitle: String,
    year: String,
    runtime: Int,
    genre: List<String>,
    posterPath: String?,
    rating: Double,
    description: String,
    castList: List<Cast> = emptyList(),
    onBackClick: () -> Unit = {},
    isFavorite : Boolean = false,
    onHeartClick: () -> Unit = {},
    onPlayClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onPersonClick: (Int) -> Unit,
    isLandscape: Boolean
) {
    if (isLandscape) {
        // 🔹 Yatay görünüm
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1a1a2e),
                            Color(0xFF16213e),
                            Color(0xFF0f1419)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            // Sol taraf: Poster
            Column(
                modifier = Modifier
                    .width(250.dp)
                    .fillMaxHeight(),
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
                Spacer(Modifier.height(16.dp))
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                ) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w300$posterPath",
                        contentDescription = "Movie Picture of $movieTitle",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            // Sağ taraf: Bilgiler + butonlar + cast
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Buraya title, yıl, süre, butonlar, açıklama ve cast ekleyebilirsin
                Text(
                    text = movieTitle,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MovieInfoItem(label = year, icon = "📅")
                        Spacer(Modifier.width(24.dp))
                        MovieInfoItem(label = "${runtime / 60}h ${runtime % 60}m", icon = "⏱️")
                        Spacer(Modifier.width(24.dp))

                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = onPlayClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFff6b35)
                            ),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.watch_trailer),
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(onClick = onHeartClick) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) Color.Red else Color.Gray
                            )
                        }
                    }
                }


                Spacer(Modifier.height(16.dp))
                Text(description, color = Color.White.copy(alpha = 0.7f))

                Spacer(Modifier.height(16.dp))
                CastSection(onPersonClick, castList)
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
                // Status Bar Space

                // Top Bar
                TopAppBar(
                    title = {
                        Text(
                            text = movieTitle,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                onHeartClick()
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) Color.Red else Color.Gray
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Movie Poster
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .width(280.dp)
                            .height(380.dp),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                    ) {
                        Box {
                            // Placeholder for movie poster - replace with actual image
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                Color(0xFF4a90e2),
                                                Color(0xFF2c5aa0),
                                                Color(0xFF1a237e)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = "https://image.tmdb.org/t/p/w500$posterPath",
                                    contentDescription = "Movie Poster",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            // Gradient overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.3f)
                                            ),
                                            startY = 200f
                                        )
                                    )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                val formattedRuntime = "${runtime / 60}h ${runtime % 60}m"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MovieInfoItem(label = year, icon = "📅")
                    MovieInfoItem(label = formattedRuntime, icon = "⏱️")
                    MovieInfoItemGenre(labelList = genre, icon = "🎬")
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⭐",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    val truncatedRating = floor(rating * 10) / 10

                    Text(
                        text = truncatedRating.toString(),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Play Button
                    Button(
                        onClick = onPlayClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFff6b35)
                        ),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.watch_trailer),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))



                    Spacer(modifier = Modifier.width(8.dp))

                    // Share Button
                    IconButton(
                        onClick = onShareClick,
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color(0xFF4ecdc4)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Story Line Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.story),
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = description,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                }

                Spacer(modifier = Modifier.height(16.dp))

                // Cast and Crew Section
                Text(
                    text = stringResource(R.string.cast_and_crew),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Cast List
                if (castList.isNotEmpty()) {
                    CastSection(onPersonClick, castList = castList)
                } else {
                    // Empty state for cast
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_cast_and_crew),
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 14.sp
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun MovieInfoItem(label: String, icon: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp
        )
    }
}
@Composable
fun MovieInfoItemGenre(labelList: List<String>, icon: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 20.sp)
        Spacer(Modifier.height(4.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            labelList.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    color = Color.White.copy(alpha = 0.7f),

                    )
            }
        }
    }
}



@Composable
fun CastSection(
    onPersonClick: (Int) -> Unit,
    castList: List<Cast>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        items(castList) { castMember ->
            CastMemberItem(onPersonClick,castMember = castMember)
        }
    }
}

@Composable
fun CastMemberItem(
    onPersonClick: (Int) -> Unit,
    castMember: Cast,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(100.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .clickable { onPersonClick(castMember.id) }
                .size(70.dp),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            if (castMember.profile_path != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(castMember.profileImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Profile of ${castMember.name}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF4a4a4a),
                                    Color(0xFF2a2a2a)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Default profile",
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Actor Real Name
        Text(
            text = castMember.name,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        // Character Name
        Text(
            text = castMember.character,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

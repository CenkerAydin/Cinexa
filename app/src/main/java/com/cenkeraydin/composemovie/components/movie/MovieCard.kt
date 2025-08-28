package com.cenkeraydin.composemovie.components.movie

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cenkeraydin.composemovie.R
import kotlin.math.floor

@Composable
fun MovieCard(
    title: String,
    year: String,
    genre: String,
    rating: Double,
    imageUrl: String,
    movieId: Int,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    val rotation by animateFloatAsState(
        targetValue = if (isFavorite) 360f else 0f,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
        label = "rotation"
    )

    val scale by animateFloatAsState(
        targetValue = if (isFavorite) 1.4f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val tint by animateColorAsState(
        targetValue = if (isFavorite) Color.Red else Color.Gray,
        animationSpec = tween(durationMillis = 300),
        label = "tint"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E2E)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_calendar_today_24),
                                contentDescription = "Year",
                                tint = Color(0xFF9CA3AF),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = year,
                                color = Color(0xFF9CA3AF),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_star_rate_24),
                                contentDescription = "Rating",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(20.dp)
                            )
                            val truncatedRating = floor(rating * 10) / 10

                            Text(
                                text = truncatedRating.toString(),
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_movie_filter_24),
                                    contentDescription = "Genre",
                                    tint = Color(0xFF9CA3AF),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "$genre | Movie",
                                    color = Color(0xFF9CA3AF),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }
            }


            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .size(36.dp)
                    .graphicsLayer {
                        rotationZ = rotation
                        scaleX = scale
                        scaleY = scale
                    }
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favori",
                    tint = tint
                )
            }
        }
    }
}


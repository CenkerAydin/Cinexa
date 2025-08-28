package com.cenkeraydin.composemovie.components.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
fun FavoriteCardGrid(
    title: String,
    rating: Double,
    imageUrl: String,
    modifier: Modifier = Modifier,
    isLandscape: Boolean = false,
    onClick: () -> Unit = {}
) {
    if (isLandscape) {
        Card(
            modifier = modifier
                .clickable { onClick() }
                .then(
                    if (isLandscape) Modifier.height(120.dp)
                    else Modifier.height(200.dp)
                ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E2E)
            )
        ) {
            // Yatay: Resim ve metin yan yana
            Row(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(100.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                            )
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_star_rate_24),
                                contentDescription = "Rating",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(16.dp)
                            )
                            val truncatedRating = floor(rating * 10) / 10
                            Text(
                                text = truncatedRating.toString(),
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        // Başlık altta
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }else {
            Card(
                modifier = modifier
                    .clickable { onClick() },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E1E2E)
                )
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Rating Badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp)
                            .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_star_rate_24),
                                contentDescription = "Rating",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(12.dp)
                            )
                            val truncatedRating = floor(rating * 10) / 10
                            Text(
                                text = truncatedRating.toString(),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    // Title alt kısımda
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.8f)
                                    )
                                )
                            )
                            .padding(8.dp)
                    ) {
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }


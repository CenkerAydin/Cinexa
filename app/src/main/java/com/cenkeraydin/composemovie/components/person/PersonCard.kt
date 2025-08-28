package com.cenkeraydin.composemovie.components.person

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cenkeraydin.composemovie.R
import com.cenkeraydin.composemovie.components.PersonFavoriteButton
import kotlin.math.floor

@Composable
fun PersonCard(
    name: String,
    knownForDepartment: String,
    popularity: Double,
    imageUrl: String,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
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
                        model = "https://image.tmdb.org/t/p/w500/$imageUrl",
                        contentDescription = "Person Image",
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
                            text = name,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Department: $knownForDepartment",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))


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
                            val truncatedRating = floor(popularity * 10) / 10

                            Text(
                                text = truncatedRating.toString(),
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }
                }
            }


            PersonFavoriteButton(
                isFavorite = isFavorite,
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
            )
        }
    }
}



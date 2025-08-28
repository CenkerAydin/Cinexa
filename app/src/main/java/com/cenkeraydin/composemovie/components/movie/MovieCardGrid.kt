package com.cenkeraydin.composemovie.components.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun MovieCardGrid(
    title: String,
    rating: Double,
    imageUrl: String,
    isLiked: Boolean = false,                    // Favori durumu parametresi
    onFavoriteClick: () -> Unit = {},            // Favori butonuna tıklama callback'i
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .aspectRatio(0.7f)
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

            // Favorite Icon (sağ üst köşede)
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(24.dp) // biraz daha büyük yapabiliriz

            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isLiked) "Favorited" else "Not Favorited",
                    tint = if (isLiked) Color(0xFFff4757) else Color.White
                )
            }

            // Title alt kısımda
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))))
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

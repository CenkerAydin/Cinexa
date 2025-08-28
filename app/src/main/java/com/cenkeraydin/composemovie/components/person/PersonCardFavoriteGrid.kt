package com.cenkeraydin.composemovie.components.person

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
fun PersonCardFavoriteGrid(
    name: String,
    profilePhotoUrl: String,
    popularity: Double,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
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
                model = profilePhotoUrl,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Popularity Badge
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
                        contentDescription = "Popularity",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(12.dp)
                    )
                    val truncatedPopularity = floor(popularity * 10) / 10
                    Text(
                        text = truncatedPopularity.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Name at the bottom
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))))
                    .padding(8.dp)
            ) {
                Text(
                    text = name,
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

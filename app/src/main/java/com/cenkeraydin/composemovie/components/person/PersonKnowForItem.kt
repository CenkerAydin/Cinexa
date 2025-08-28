package com.cenkeraydin.composemovie.components.person

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cenkeraydin.composemovie.data.model.person.CombinedCreditItem

@Composable
fun PersonKnowForItem(item: CombinedCreditItem, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.height(180.dp).fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            if (item.posterPath != null) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w185${item.posterPath}",
                    contentDescription = item.originalTitle ?: item.originalName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
            text = item.originalTitle ?: item.originalName ?: "",
            color = Color.White,
            fontSize = 12.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

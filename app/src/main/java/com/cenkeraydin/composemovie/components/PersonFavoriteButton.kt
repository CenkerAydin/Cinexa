package com.cenkeraydin.composemovie.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun PersonFavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var trigger by remember { mutableStateOf(false) }

    val rippleProgress by animateFloatAsState(
        targetValue = if (trigger) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "rippleProgress"
    )

    if (trigger) {
        LaunchedEffect(trigger) {
            delay(500)
            trigger = false
        }
    }

    Box(
        modifier = modifier.size(60.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            if (rippleProgress > 0f) {
                drawCircle(
                    color = if (isFavorite) Color.Red.copy(alpha = 0.3f) else Color.Gray.copy(alpha = 0.3f),
                    radius = size.minDimension / 2 * rippleProgress
                )
            }
        }

        IconButton(
            onClick = {
                onClick()
                trigger = true
            },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favori",
                tint = if (isFavorite) Color.Red else Color.Gray
            )
        }
    }
}


package com.cenkeraydin.composemovie.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_series")
data class FavoriteSerieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String?,
    val firstAirDate: String,
    val voteAverage: Double
)
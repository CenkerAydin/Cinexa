package com.cenkeraydin.composemovie.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_persons")
data class FavoritePersonEntity(
    @PrimaryKey val id: Int,
    val name: String?,
    val profilePath: String?,
    val popularity: Double
)
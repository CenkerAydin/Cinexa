package com.cenkeraydin.composemovie.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteMovieEntity::class,FavoriteSerieEntity::class,FavoritePersonEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
    abstract fun favoriteSerieDao(): FavoriteSerieDao
    abstract fun favoritePersonDao(): FavoritePersonDao
}

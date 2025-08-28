package com.cenkeraydin.composemovie.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: FavoriteMovieEntity)

    @Delete
    suspend fun delete(movie: FavoriteMovieEntity)

    @Query("SELECT * FROM favorite_movies")
    fun getAllFavorites(): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    fun isFavorite(movieId: Int): Flow<Boolean>
}

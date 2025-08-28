package com.cenkeraydin.composemovie.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteSerieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSerie(movie: FavoriteSerieEntity)

    @Delete
    suspend fun deleteSerie(movie: FavoriteSerieEntity)

    @Query("SELECT * FROM favorite_series")
    fun getAllFavoritesSerie(): Flow<List<FavoriteSerieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_series WHERE id = :serieId)")
    fun isFavoriteSerie(serieId: Int): Flow<Boolean>
}

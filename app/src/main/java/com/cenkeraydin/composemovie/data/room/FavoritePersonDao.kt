package com.cenkeraydin.composemovie.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(person: FavoritePersonEntity)

    @Delete
    suspend fun delete(person: FavoritePersonEntity)

    @Query("SELECT * FROM favorite_persons")
    fun getAllFavorites(): Flow<List<FavoritePersonEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_persons WHERE id = :personId)")
    fun isFavorite(personId: Int): Flow<Boolean>
}

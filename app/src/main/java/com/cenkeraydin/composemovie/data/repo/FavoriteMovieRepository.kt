package com.cenkeraydin.composemovie.data.repo

import com.cenkeraydin.composemovie.data.room.FavoriteMovieDao
import com.cenkeraydin.composemovie.data.room.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteMovieRepository @Inject constructor(
    private val dao: FavoriteMovieDao
) {
    fun isFavorite(id: Int): Flow<Boolean> = dao.isFavorite(id)

    fun getAllFavorites(): Flow<List<FavoriteMovieEntity>> = dao.getAllFavorites()

    suspend fun addToFavorites(movie: FavoriteMovieEntity) = dao.insert(movie)

    suspend fun removeFromFavorites(movie: FavoriteMovieEntity) = dao.delete(movie)
}

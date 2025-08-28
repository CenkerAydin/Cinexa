package com.cenkeraydin.composemovie.data.repo

import com.cenkeraydin.composemovie.data.room.FavoriteMovieDao
import com.cenkeraydin.composemovie.data.room.FavoriteMovieEntity
import com.cenkeraydin.composemovie.data.room.FavoriteSerieDao
import com.cenkeraydin.composemovie.data.room.FavoriteSerieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteSerieRepository @Inject constructor(
    private val dao: FavoriteSerieDao
) {
    fun isFavoriteSerie(id: Int): Flow<Boolean> = dao.isFavoriteSerie(id)

    fun getAllFavorites(): Flow<List<FavoriteSerieEntity>> = dao.getAllFavoritesSerie()

    suspend fun addToFavorites(movie: FavoriteSerieEntity) = dao.insertSerie(movie)

    suspend fun removeFromFavorites(movie: FavoriteSerieEntity) = dao.deleteSerie(movie)
}

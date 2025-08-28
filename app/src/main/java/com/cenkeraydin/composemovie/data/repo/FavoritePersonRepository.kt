package com.cenkeraydin.composemovie.data.repo

import com.cenkeraydin.composemovie.data.room.FavoritePersonDao
import com.cenkeraydin.composemovie.data.room.FavoritePersonEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritePersonRepository @Inject constructor(
    private val dao: FavoritePersonDao
) {
    fun isFavoritePerson(id: Int): Flow<Boolean> = dao.isFavorite(id)

    fun getAllFavorites(): Flow<List<FavoritePersonEntity>> = dao.getAllFavorites()

    suspend fun addToFavorites(person: FavoritePersonEntity) = dao.insert(person)

    suspend fun removeFromFavorites(person: FavoritePersonEntity) = dao.delete(person)
}
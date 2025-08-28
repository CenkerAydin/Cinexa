package com.cenkeraydin.composemovie.ui.favorite

import androidx.lifecycle.ViewModel
import com.cenkeraydin.composemovie.data.repo.FavoriteMovieRepository
import com.cenkeraydin.composemovie.data.repo.FavoritePersonRepository
import com.cenkeraydin.composemovie.data.repo.FavoriteSerieRepository
import com.cenkeraydin.composemovie.data.room.FavoriteMovieEntity
import com.cenkeraydin.composemovie.data.room.FavoritePersonEntity
import com.cenkeraydin.composemovie.data.room.FavoriteSerieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteMovieRepository,
    private val favoriteSerieRepository: FavoriteSerieRepository,
    private val favoritePersonRepository: FavoritePersonRepository
) :ViewModel() {

    private val _movieFavorites = MutableStateFlow<List<FavoriteMovieEntity>>(emptyList())
    val movieFavorites = _movieFavorites

    private val _serieFavorites = MutableStateFlow<List<FavoriteSerieEntity>>(emptyList())
    val serieFavorites = _serieFavorites

    private val _personFavorites = MutableStateFlow<List<FavoritePersonEntity>>(emptyList())
    val personFavorites = _personFavorites

    suspend fun getAllFavorites() {
        favoriteRepository.getAllFavorites().collect { favoriteList ->
            _movieFavorites.value = favoriteList
        }
    }

    suspend fun getAllSerieFavorites() {
        favoriteSerieRepository.getAllFavorites().collect { favoriteList ->
            _serieFavorites.value = favoriteList
        }
    }

    suspend fun getAllPersonFavorites() {
        favoritePersonRepository.getAllFavorites().collect { favoriteList ->
            _personFavorites.value = favoriteList
        }
    }


}
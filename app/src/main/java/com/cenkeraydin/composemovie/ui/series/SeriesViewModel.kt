package com.cenkeraydin.composemovie.ui.series

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cenkeraydin.composemovie.data.model.movie.Movie
import com.cenkeraydin.composemovie.data.model.series.Series
import com.cenkeraydin.composemovie.data.repo.FavoriteSerieRepository
import com.cenkeraydin.composemovie.data.retrofit.TMDBService
import com.cenkeraydin.composemovie.data.room.FavoriteMovieEntity
import com.cenkeraydin.composemovie.data.room.FavoriteSerieEntity
import com.cenkeraydin.composemovie.util.Constants.API_KEY
import com.cenkeraydin.composemovie.util.MovieFilter
import com.cenkeraydin.composemovie.util.SeriesFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val service: TMDBService,
    private val favoriteRepository: FavoriteSerieRepository
): ViewModel() {

    private val _series = MutableStateFlow<List<Series>>(emptyList())
    val series: StateFlow<List<Series>> = _series.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _genres = MutableStateFlow<Map<Int, String>>(emptyMap())
    val genres: StateFlow<Map<Int, String>> = _genres.asStateFlow()

    private val _selectedGenreId = MutableStateFlow<Int?>(null)
    val selectedGenreId: StateFlow<Int?> = _selectedGenreId.asStateFlow()

    val language = Locale.getDefault().toLanguageTag()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 1
    private var endReached = false
    @RequiresApi(Build.VERSION_CODES.O)

    private val _seriesFilter = MutableStateFlow(SeriesFilter.POPULAR)
    val seriesFilter: StateFlow<SeriesFilter> = _seriesFilter

    fun setSeriesFilter(filter: SeriesFilter) {
        _seriesFilter.value = filter
        _series.value = emptyList()
        currentPage = 1
        endReached = false
        _selectedGenreId.value = null

        when (filter) {
            SeriesFilter.POPULAR -> fetchPopularSeries()
            SeriesFilter.TRENDING -> fetchTrendingSeries()
            SeriesFilter.TOP_RATED -> fetchTopRatedSeries()
        }
    }

    fun fetchTrendingSeries() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = service.getTrendingTVShows(
                    apiKey = API_KEY,
                    timeWindow = "week",
                    language = "en-US",
                    page = currentPage
                )
                _series.value = response.results
            } catch (e: Exception) {
                _series.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchTopRatedSeries() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = service.getTopRatedTVShows(
                    apiKey = API_KEY,
                    language = "en-US",
                    page = currentPage
                )
                _series.value = response.results
            } catch (e: Exception) {
                _series.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun fetchPopularSeries() {
        if (_isLoading.value || endReached) return

        _isLoading.value = true
        viewModelScope.launch {
            try {

                val genreResponse = service.getTVGenres(API_KEY, language)
                _genres.value = genreResponse.genres.associateBy({ it.id }, { it.name })

                val response = service.getTVShows(
                    apiKey = API_KEY,
                    firstAirDateGte = "",
                    sortBy = "popularity.desc",
                    language = "en-US",
                    page = currentPage
                )
                val newSeries = response.results

                if (newSeries.isEmpty()) {
                    endReached = true
                } else {
                    _series.value += newSeries
                    currentPage++
                }
            } catch (_: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }


    init {
        setSeriesFilter(SeriesFilter.POPULAR)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun searchTV(query: String) {
        _searchQuery.value = query

        if (query.isBlank()) {
            selectGenreSerie(_selectedGenreId.value)
            currentPage = 1
            when (_seriesFilter.value) {
                SeriesFilter.TRENDING -> fetchTrendingSeries()
                SeriesFilter.POPULAR -> fetchPopularSeries()
                SeriesFilter.TOP_RATED -> fetchTopRatedSeries()
            }
            return
        }

        viewModelScope.launch {
            try {
                val response = when (_seriesFilter.value) {
                    SeriesFilter.TRENDING -> {
                        service.searchTVShows(
                            query = query,
                            apiKey = API_KEY,
                            page = 1,
                            includeAdult = false, // Trending için genellikle yetişkin içeriği hariç tutulur
                        )
                    }
                    SeriesFilter.POPULAR -> {
                        service.searchTVShows(
                            query = query,
                            apiKey = API_KEY,
                            page = 1,
                        )
                    }
                    SeriesFilter.TOP_RATED -> {
                        service.searchTVShows(
                            query = query,
                            apiKey = API_KEY,
                            page = 1,
                        )
                    }

                }

                if (response.isSuccessful) {
                    _series.value = response.body()?.results ?: emptyList()
                } else {
                    _series.value = emptyList()
                }
            } catch (e: Exception) {
                _series.value = emptyList()
            }
        }
    }

    fun isFavorite(movieId: Int): Flow<Boolean> = favoriteRepository.isFavoriteSerie(movieId)

    fun toggleFavorite(serie: Series) {
        viewModelScope.launch {
            val isFav = isFavorite(serie.id).first()
            val entity = FavoriteSerieEntity(
                id = serie.id,
                title = serie.name,
                posterPath = serie.posterPath,
                firstAirDate = serie.firstAirDate,
                voteAverage = serie.voteAverage
            )

            if (isFav) {
                favoriteRepository.removeFromFavorites(entity)
            } else {
                favoriteRepository.addToFavorites(entity)
            }
        }
    }

    private fun getSeriesByGenre(genreId: Int) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val series = when (_seriesFilter.value) {
                    SeriesFilter.POPULAR -> {
                        service.discoverSeriesByGenre(
                            genreId = genreId,
                            apiKey = API_KEY,
                            language = language
                        ).results
                    }
                    SeriesFilter.TOP_RATED -> {
                        service.discoverSeriesByGenre(
                            genreId = genreId,
                            apiKey = API_KEY,
                            language = language
                        ).results
                    }
                    SeriesFilter.TRENDING -> {
                        val response = service.getTrendingTVShows(
                            timeWindow = "week",
                            apiKey = API_KEY,
                            page = 1,
                            language = language
                        ).results

                        response.filter { it.genreIds.contains(genreId) }
                    }
                }

                val translatedFiltered = filterTranslatedSeries(series)
                _series.value = translatedFiltered

            } catch (e: Exception) {
                _series.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }private fun filterTranslatedSeries(series: List<Series>): List<Series> {
        return series.filter { series ->
            val hasCJK = series.name.any {
                it in '\u3040'..'\u30FF' || it in '\u4E00'..'\u9FBF'
            }
            !hasCJK
        }
    }

    fun selectGenreSerie(genreId: Int?) {
        _selectedGenreId.value = genreId

        if (genreId == null) {
            _series.value = emptyList()
            currentPage = 1
            endReached = false
            setSeriesFilter(_seriesFilter.value)
        } else {
            getSeriesByGenre(genreId)
        }
    }

}
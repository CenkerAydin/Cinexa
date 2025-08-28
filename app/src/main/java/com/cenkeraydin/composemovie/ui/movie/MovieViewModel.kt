package com.cenkeraydin.composemovie.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cenkeraydin.composemovie.data.model.movie.Movie
import com.cenkeraydin.composemovie.data.repo.FavoriteMovieRepository
import com.cenkeraydin.composemovie.data.retrofit.TMDBService
import com.cenkeraydin.composemovie.data.room.FavoriteMovieEntity
import com.cenkeraydin.composemovie.util.Constants.API_KEY
import com.cenkeraydin.composemovie.util.MovieFilter
import com.cenkeraydin.composemovie.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val service: TMDBService,
    private val favoriteRepository: FavoriteMovieRepository
) : ViewModel() {

    private val _movies = MutableStateFlow<UIState<List<Movie>>>(UIState.Loading)
    val movies: StateFlow<UIState<List<Movie>>> = _movies.asStateFlow()

    private val _genres = MutableStateFlow<Map<Int, String>>(emptyMap())
    val genres: StateFlow<Map<Int, String>> = _genres.asStateFlow()

    private val _selectedGenreId = MutableStateFlow<Int?>(null)
    val selectedGenreId: StateFlow<Int?> = _selectedGenreId.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val language = Locale.getDefault().toLanguageTag()
    private var currentPage = 1
    private var endReached = false

    private val _movieFilter = MutableStateFlow(MovieFilter.POPULAR)
    val movieFilter: StateFlow<MovieFilter> = _movieFilter

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        setMovieFilter(MovieFilter.POPULAR)
    }

    fun setMovieFilter(filter: MovieFilter) {
        _movieFilter.value = filter
        _movies.value = UIState.Loading
        currentPage = 1
        endReached = false
        _selectedGenreId.value = null

        when (filter) {
            MovieFilter.POPULAR -> fetchPopularMovies()
            MovieFilter.TRENDING -> fetchTrendingMovies()
            MovieFilter.TOP_RATED -> fetchTopRatedMovies()
        }
    }

    private fun filterTranslatedMovies(movies: List<Movie>): List<Movie> {
        return movies.filter { movie ->
            val hasCJK = movie.title.any {
                it in '\u3040'..'\u30FF' || it in '\u4E00'..'\u9FBF'
            }
            !hasCJK
        }
    }

    private fun handleError(e: Exception) {
        val errorMessage = if (e.message?.contains("Unable to resolve host", ignoreCase = true) == true) {
            "Lütfen internet bağlantınızı kontrol edin."
        } else {
            "Film bulunamadı. 🎬"
        }
        _movies.value = UIState.Error(errorMessage)
    }

    fun fetchTopRatedMovies() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = service.getTopRatedMovies(
                    apiKey = API_KEY,
                    page = currentPage,
                    language = language
                )
                val newMovies = filterTranslatedMovies(response.results)

                if (newMovies.isNotEmpty()) {
                    val currentList = (_movies.value as? UIState.Success)?.data ?: emptyList()
                    _movies.value = UIState.Success(currentList + newMovies)
                    currentPage++
                } else {
                    endReached = true
                    if (currentPage == 1) _movies.value = UIState.Error("Film bulunamadı. 🎬")
                }
            } catch (e: Exception) {
                handleError(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchTrendingMovies() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = service.getTrendingMovies(
                    timeWindow = "week",
                    apiKey = API_KEY,
                    page = currentPage,
                    language = language
                )
                val newMovies = filterTranslatedMovies(response.results)

                if (newMovies.isNotEmpty()) {
                    val currentList = (_movies.value as? UIState.Success)?.data ?: emptyList()
                    _movies.value = UIState.Success(currentList + newMovies)
                    currentPage++
                } else {
                    endReached = true
                    if (currentPage == 1) _movies.value = UIState.Error("Film bulunamadı. 🎬")
                }
            } catch (e: Exception) {
                handleError(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchPopularMovies() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val genreResponse = service.getGenres(API_KEY, language)
                _genres.value = genreResponse.genres.associateBy({ it.id }, { it.name })

                val response = service.getPopularMovies(apiKey = API_KEY, page = currentPage, language = language)
                val newMovies = filterTranslatedMovies(response.results)

                if (newMovies.isNotEmpty()) {
                    val currentList = (_movies.value as? UIState.Success)?.data ?: emptyList()
                    _movies.value = UIState.Success(currentList + newMovies)
                    currentPage++
                } else {
                    endReached = true
                    if (currentPage == 1) _movies.value = UIState.Error("Film bulunamadı. 🎬")
                }
            } catch (e: Exception) {
                handleError(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun getMoviesByGenre(genreId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val movies = when (_movieFilter.value) {
                    MovieFilter.POPULAR, MovieFilter.TOP_RATED -> {
                        service.discoverMoviesByGenre(genreId, API_KEY, language).results
                    }
                    MovieFilter.TRENDING -> {
                        val response = service.getTrendingMovies(
                            timeWindow = "week",
                            apiKey = API_KEY,
                            page = 1,
                            language = language
                        ).results

                        response.filter { it.genreIds.contains(genreId) }
                    }
                }
                val filtered = filterTranslatedMovies(movies)
                if (filtered.isNotEmpty()) {
                    _movies.value = UIState.Success(filtered)
                } else {
                    _movies.value = UIState.Error("Film bulunamadı. 🎬")
                }
            } catch (e: Exception) {
                handleError(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectGenre(genreId: Int?) {
        _selectedGenreId.value = genreId
        if (genreId == null) {
            currentPage = 1
            endReached = false
            setMovieFilter(_movieFilter.value)
        } else {
            getMoviesByGenre(genreId)
        }
    }

    fun searchMovies(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            setMovieFilter(_movieFilter.value)
            return
        }
        viewModelScope.launch {
            try {
                val response = when(_movieFilter.value) {
                    MovieFilter.TRENDING -> {
                        service.searchMovies(
                            query = query,
                            apiKey = API_KEY,
                            page = currentPage,
                            language = language
                        )
                    }
                    MovieFilter.POPULAR, MovieFilter.TOP_RATED -> {
                        service.searchMovies(
                            query = query,
                            apiKey = API_KEY,
                            page = currentPage,
                            language = language
                        )
                    }
                }
                if (response.results.isNotEmpty()) {
                    _movies.value = UIState.Success(response.results)
                } else {
                    _movies.value = UIState.Error("Film bulunamadı. 🎬")
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun isFavorite(movieId: Int): Flow<Boolean> = favoriteRepository.isFavorite(movieId)

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            val isFav = isFavorite(movie.id).first()
            val entity = FavoriteMovieEntity(
                id = movie.id,
                title = movie.title,
                posterPath = movie.posterPath,
                releaseDate = movie.releaseDate,
                voteAverage = movie.voteAverage
            )
            if (isFav) {
                favoriteRepository.removeFromFavorites(entity)
            } else {
                favoriteRepository.addToFavorites(entity)
            }
        }
    }

    fun fetchNextPageIfNeeded() {
        if (!endReached && !_isLoading.value) {
            when (_movieFilter.value) {
                MovieFilter.POPULAR -> fetchPopularMovies()
                MovieFilter.TOP_RATED -> fetchTopRatedMovies()
                MovieFilter.TRENDING -> fetchTrendingMovies()
            }
        }
    }
}

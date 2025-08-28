package com.cenkeraydin.composemovie.ui.person

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cenkeraydin.composemovie.data.model.person.Person
import com.cenkeraydin.composemovie.data.repo.FavoritePersonRepository
import com.cenkeraydin.composemovie.data.retrofit.TMDBService
import com.cenkeraydin.composemovie.data.room.FavoritePersonEntity
import com.cenkeraydin.composemovie.util.Constants.API_KEY
import com.cenkeraydin.composemovie.util.MovieFilter
import com.cenkeraydin.composemovie.util.PersonFilter
import com.cenkeraydin.composemovie.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val service: TMDBService,
    private val favoriteRepository: FavoritePersonRepository
) : ViewModel() {

    private val _persons = MutableStateFlow<UIState<List<Person>>>(UIState.Loading)
    val persons: StateFlow<UIState<List<Person>>> = _persons.asStateFlow()

    private var currentPage = 1
    private var endReached = false
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val loadedPersons = mutableListOf<Person>()

    private val _personFilter = MutableStateFlow(PersonFilter.POPULAR)
    val personFilter: StateFlow<PersonFilter> = _personFilter

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val isEnglishOnly: (String) -> Boolean = { name ->
        name.matches(Regex("^[A-Za-z0-9 .'-]+$"))
    }

    init {
        setPersonFilter(PersonFilter.POPULAR)
    }

    fun setPersonFilter(filter: PersonFilter) {
        _personFilter.value = filter
        _persons.value = UIState.Loading
        resetPagination()

        when (filter) {
            PersonFilter.POPULAR -> fetchPerson()
            PersonFilter.TRENDING -> fetchTrendPerson()
        }
    }

    private fun resetPagination() {
        currentPage = 1
        endReached = false
        loadedPersons.clear()
    }

    fun searchPersons(query: String) {
        _searchQuery.value = query

        if (query.isBlank()) {
            resetPagination()
            when (_personFilter.value) {
                PersonFilter.TRENDING -> fetchTrendPerson()
                PersonFilter.POPULAR -> fetchPerson()
            }
            return
        }

        if (currentPage == 1) {
            _persons.value = UIState.Loading
            loadedPersons.clear()
            endReached = false
        }

        if (_isLoading.value || endReached) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = service.searchPerson(
                    query = query,
                    apiKey = API_KEY,
                    page = currentPage,
                    language = "en-US",
                    includeAdult = false
                )

                val filteredResults = response.results.filter {
                    it.name!!.matches(Regex("^[A-Za-z0-9 .'-]+$"))
                }

                if (filteredResults.isNotEmpty()) {
                    loadedPersons.addAll(filteredResults)
                    _persons.value = UIState.Success(loadedPersons.toList())
                    currentPage++
                } else {
                    if (currentPage == 1) {
                        _persons.value = UIState.Success(emptyList())
                    }
                    endReached = true
                }
            } catch (e: Exception) {
                _persons.value = UIState.Error("Arama sırasında hata oluştu: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchTrendPerson() {
        if (_searchQuery.value.isNotBlank()) return

        if (_isLoading.value || endReached) return

        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = service.getTrendingPerson(
                    apiKey = API_KEY,
                    timeWindow = "week",
                    language = "en-US",
                    page = currentPage
                )

                val filteredResults = response.results.filter { person ->
                    person.name?.let { isEnglishOnly(it) } == true
                }

                if (filteredResults.isNotEmpty()) {
                    loadedPersons.addAll(filteredResults)
                    _persons.value = UIState.Success(loadedPersons.toList())
                    currentPage++
                } else {
                    endReached = true
                }
            } catch (e: Exception) {
                _persons.value = UIState.Error("Trend kişiler alınamadı: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchPerson() {
        if (_searchQuery.value.isNotBlank()) return

        if (_isLoading.value || endReached) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = service.getPopularPersons(API_KEY, page = currentPage)
                if (response.isSuccessful && response.body() != null) {
                    val newPersons = response.body()!!.results
                    Log.d("PersonViewModel", "Fetched Page $currentPage, Persons: ${newPersons.size}")

                    if (newPersons.isNotEmpty()) {
                        loadedPersons.addAll(newPersons)
                        _persons.value = UIState.Success(loadedPersons.toList())
                        currentPage++
                    } else {
                        endReached = true
                    }
                } else {
                    _persons.value = UIState.Error("Kişi bilgisi alınamadı")
                }
            } catch (e: Exception) {
                _persons.value = UIState.Error("Hata: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun isFavorite(personId: Int): Flow<Boolean> = favoriteRepository.isFavoritePerson(personId)

    fun toggleFavorite(person: Person) {
        viewModelScope.launch {
            val isFav = isFavorite(person.id).first()
            val entity = FavoritePersonEntity(
                id = person.id,
                name = person.name,
                profilePath = person.profilePath,
                popularity = person.popularity,
            )

            if (isFav) {
                favoriteRepository.removeFromFavorites(entity)
            } else {
                favoriteRepository.addToFavorites(entity)
            }
        }
    }

    fun loadMoreIfNeeded() {
        if (_isLoading.value || endReached) return

        when {
            _searchQuery.value.isNotBlank() -> {
                searchPersons(_searchQuery.value)
            }
            _personFilter.value == PersonFilter.TRENDING -> {
                fetchTrendPerson()
            }
            _personFilter.value == PersonFilter.POPULAR -> {
                fetchPerson()
            }
        }
    }

    fun startNewSearch(query: String) {
        resetPagination()
        searchPersons(query)
    }
}
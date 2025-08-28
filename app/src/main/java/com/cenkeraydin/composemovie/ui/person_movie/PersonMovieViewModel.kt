package com.cenkeraydin.composemovie.ui.person_movie


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cenkeraydin.composemovie.data.model.movie.CastMovie
import com.cenkeraydin.composemovie.data.model.movie.PersonMovieDetails
import com.cenkeraydin.composemovie.data.retrofit.TMDBService
import com.cenkeraydin.composemovie.util.Constants.API_KEY
import com.cenkeraydin.composemovie.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PersonMovieViewModel @Inject constructor(private val service: TMDBService) : ViewModel() {

    private val _personDetails = MutableStateFlow<UIState<PersonMovieDetails>>(UIState.Loading)
    val personDetails: StateFlow<UIState<PersonMovieDetails>> = _personDetails

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _movieCredits = MutableStateFlow<List<CastMovie>>(emptyList())
    val movieCredits: StateFlow<List<CastMovie>> = _movieCredits.asStateFlow()

    val language = Locale.getDefault().toLanguageTag()


    fun fetchPersonMovieCredits(personId: Int) {
        viewModelScope.launch {
            try {
                val response = service.getPersonMovieCredits(personId, API_KEY,language)
                Log.e("PersonMovieViewModel", "Response: ${response.body()}")
                if (response.isSuccessful) {
                    _movieCredits.value = response.body()?.cast?.sortedByDescending {
                        it.release_date ?: ""
                    } ?: emptyList()
                }
            } catch (_: Exception) {
            }
        }
    }
    fun fetchPersonDetails(personId: Int) {
        viewModelScope.launch {
            _personDetails.value = UIState.Loading
            try {
                val response = service.getPersonMovieDetail(personId, API_KEY,language)
                if (response.isSuccessful && response.body() != null) {
                    _personDetails.value = UIState.Success(response.body()!!)
                } else {
                    _personDetails.value = UIState.Error("Kişi bilgisi alınamadı")
                }
            } catch (e: Exception) {
                _personDetails.value = UIState.Error("Hata: ${e.localizedMessage}")
            }
        }
    }
}
package com.cenkeraydin.composemovie.ui.person_serie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cenkeraydin.composemovie.data.model.series.CastSerie
import com.cenkeraydin.composemovie.data.model.series.PersonSerieDetails
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
class PersonSerieViewModel @Inject constructor(private val service: TMDBService) : ViewModel() {

    private val _personSerieDetails = MutableStateFlow<UIState<PersonSerieDetails>>(UIState.Loading)
    val personSerieDetails: StateFlow<UIState<PersonSerieDetails>> = _personSerieDetails

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _serieCredits = MutableStateFlow<List<CastSerie>>(emptyList())
    val serieCredits: StateFlow<List<CastSerie>> = _serieCredits.asStateFlow()

    val language = Locale.getDefault().toLanguageTag()


    fun fetchPersonSerieCredits(personId: Int) {
        viewModelScope.launch {
            try {
                val response = service.getPersonSerieCredits(personId, API_KEY)
                if (response.isSuccessful) {
                    Log.e("PersonSerieViewModel", "Response: ${response.body()}")
                    _serieCredits.value = response.body()?.cast!!
                    Log.d("PersonSerieViewModel", "Fetched Serie Credits: ${_serieCredits.value.size} items")
                }
            } catch (_: Exception) {
            }
        }
    }

    fun fetchPersonDetails(personId: Int) {
        viewModelScope.launch {
            _personSerieDetails.value = UIState.Loading
            try {
                val response = service.getPersonSerieDetail(personId, API_KEY,language)
                if (response.isSuccessful && response.body() != null) {
                    Log.d("PersonSerieViewModel", "Fetched Person Details: ${response.body()}")
                    _personSerieDetails.value = UIState.Success(response.body()!!)
                } else {
                    _personSerieDetails.value = UIState.Error("Kişi bilgisi alınamadı")
                }
            } catch (e: Exception) {
                _personSerieDetails.value = UIState.Error("Hata: ${e.localizedMessage}")
            }
        }
    }
}
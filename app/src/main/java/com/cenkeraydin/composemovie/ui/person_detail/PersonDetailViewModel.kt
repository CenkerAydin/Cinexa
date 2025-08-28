package com.cenkeraydin.composemovie.ui.person_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cenkeraydin.composemovie.data.model.person.CombinedCreditItem
import com.cenkeraydin.composemovie.data.model.person.PersonDetails
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
class PersonDetailViewModel  @Inject constructor(
    private val service:TMDBService
): ViewModel(){

    private val _personDetails = MutableStateFlow<UIState<PersonDetails>>(UIState.Loading)
    val personDetails: StateFlow<UIState<PersonDetails>> = _personDetails

    private val _combinedCredits = MutableStateFlow<List<CombinedCreditItem>>(emptyList())
    val combinedCredits: StateFlow<List<CombinedCreditItem>> = _combinedCredits.asStateFlow()

    val language = Locale.getDefault().toLanguageTag()

    fun fetchPersonDetails(personId: Int) {
        viewModelScope.launch {
            _personDetails.value = UIState.Loading
            try {
                val response = service.getPersonDetail(personId, API_KEY,language)
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

    fun fetchCombinedCredits(personId: Int) {
        viewModelScope.launch {
            try {
                val response = service.getPersonCombinedCredits(personId, API_KEY)
                if (response.isSuccessful && response.body() != null) {
                    _combinedCredits.value = response.body()!!.cast
                } else {
                    Log.e("CombinedCredits", "API error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("CombinedCredits", "Error: ${e.message}")
            }
        }
    }



}
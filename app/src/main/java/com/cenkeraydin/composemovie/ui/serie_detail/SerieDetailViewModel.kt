package com.cenkeraydin.composemovie.ui.serie_detail

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.cenkeraydin.composemovie.R
import com.cenkeraydin.composemovie.data.model.Cast
import com.cenkeraydin.composemovie.data.model.Video
import com.cenkeraydin.composemovie.data.model.series.SerieDetailResponse
import com.cenkeraydin.composemovie.data.repo.FavoriteSerieRepository
import com.cenkeraydin.composemovie.data.retrofit.TMDBService
import com.cenkeraydin.composemovie.data.room.FavoriteSerieEntity
import com.cenkeraydin.composemovie.util.Constants.API_KEY
import com.cenkeraydin.composemovie.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SerieDetailViewModel @Inject constructor(
    private val service: TMDBService,
    private val favoriteRepository: FavoriteSerieRepository
) : ViewModel(){

    private val _serieDetail = MutableStateFlow<UIState<SerieDetailResponse>>(UIState.Loading)
    val serieDetail: StateFlow<UIState<SerieDetailResponse>> = _serieDetail

    private val _castList = MutableStateFlow<List<Cast>>(emptyList())
    val castList: StateFlow<List<Cast>> = _castList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _videoState = MutableStateFlow<UIState<List<Video>>?>(null)
    val videoState: StateFlow<UIState<List<Video>>?> = _videoState

    val language = Locale.getDefault().toLanguageTag()

    fun fetchSerieDetail(serieId: Int) {

        viewModelScope.launch {
            _serieDetail.value = UIState.Loading
            try {
                val response = service.getTVShowDetail(serieId,API_KEY, language)
                _serieDetail.value = UIState.Success(response)
            } catch (e: Exception) {
                _serieDetail.value = UIState.Error("Dizi detayları alınamadı: ${e.localizedMessage}")
            }
        }
    }

    fun getSerieCredits(movieId: Int) {
        viewModelScope.launch {

            try {
                val response = service.getSerieCredits(movieId, API_KEY)
                if (response.isSuccessful) {
                    _castList.value = response.body()?.cast ?: emptyList()
                } else {
                    _errorMessage.value = "Oyuncular yüklenemedi"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Hata: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun isFavorite(movieId: Int): Flow<Boolean> = favoriteRepository.isFavoriteSerie(movieId)

    fun toggleFavorite(movie: SerieDetailResponse) {
        viewModelScope.launch {
            val isFav = isFavorite(movie.id).first()
            val entity = FavoriteSerieEntity(
                id = movie.id,
                title = movie.name,
                posterPath = movie.posterPath,
                firstAirDate = movie.firstAirDate,
                voteAverage = movie.voteAverage
            )

            if (isFav) {
                favoriteRepository.removeFromFavorites(entity)
            } else {
                favoriteRepository.addToFavorites(entity)
            }
        }
    }

    fun saveImageToCacheAndGetUri(context: Context, bitmap: Bitmap): Uri {
        val imagesFolder = File(context.cacheDir, "shared_images")
        imagesFolder.mkdirs()

        val file = File(imagesFolder, "shared_image.png")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }

    suspend fun shareSerieWithPoster(context: Context, serieDetail: SerieDetailResponse) {
        val posterUrl = "https://image.tmdb.org/t/p/w500${serieDetail.posterPath}"
        val shareTitle = context.getString(R.string.share_serie_title)
        val shareText = context.getString(
            R.string.share_serie_text,
            serieDetail.name,
            "https://www.themoviedb.org/tv/${serieDetail.id}"
        )

        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(posterUrl)
            .allowHardware(false)
            .build()

        val result = (imageLoader.execute(request) as? SuccessResult)?.drawable

        result?.let { drawable ->
            val bitmap = (drawable as BitmapDrawable).bitmap
            val imageUri = saveImageToCacheAndGetUri(context, bitmap)

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, imageUri)
                putExtra(Intent.EXTRA_TEXT, shareText)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, shareTitle))
        }
    }

    fun getSerieVideos(serieId: Int, onOpenYouTube: (String) -> Unit) {
        viewModelScope.launch {
            _videoState.value = UIState.Loading

            try {
                val response = service.getSerieVideos(serieId, API_KEY)

                if (response.results.isNotEmpty()) {
                    _videoState.value = UIState.Success(response.results)

                    val trailer = response.results.find { video ->
                        video.site.equals("YouTube", ignoreCase = true) &&
                                (video.type.equals("Trailer", ignoreCase = true) ||
                                        video.type.equals("Teaser", ignoreCase = true)) &&
                                video.official
                    } ?: response.results.find { video ->
                        video.site.equals("YouTube", ignoreCase = true) &&
                                video.type.equals("Trailer", ignoreCase = true)
                    } ?: response.results.find { video ->
                        video.site.equals("YouTube", ignoreCase = true)
                    }

                    if (trailer != null) {
                        onOpenYouTube(trailer.key)
                    } else {
                        _videoState.value = UIState.Error("YouTube fragmanı bulunamadı")
                    }
                } else {
                    _videoState.value = UIState.Error("Bu film için fragman bulunamadı")
                }

            } catch (e: Exception) {
                _videoState.value = UIState.Error("Fragman yüklenirken hata oluştu: ${e.localizedMessage}")
            }
        }
    }







}
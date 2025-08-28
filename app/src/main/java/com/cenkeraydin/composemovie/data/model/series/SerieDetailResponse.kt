package com.cenkeraydin.composemovie.data.model.series

import com.cenkeraydin.composemovie.data.model.Genre
import com.google.gson.annotations.SerializedName

data class SerieDetailResponse(
    val id: Int,
    val name: String,
    val overview: String,
    @SerializedName("poster_path")val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("first_air_date")val firstAirDate: String,
    @SerializedName("last_air_date")val lastAirDate: String,
    @SerializedName("number_of_seasons")val numberOfSeasons: Int,
    @SerializedName("number_of_episodes")val numberOfEpisodes: Int,
    @SerializedName("vote_average")val voteAverage: Double,
    val genres: List<Genre>,
)
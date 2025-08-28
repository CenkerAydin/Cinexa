package com.cenkeraydin.composemovie.data.model.series

import com.google.gson.annotations.SerializedName

data class SeriesResponse(
    @SerializedName("results") val results: List<Series>
)

data class Series(
    val id: Int,
    val name: String,
    val originalTitle: String,
    @SerializedName("genre_ids")val genreIds: List<Int>,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("first_air_date") val firstAirDate: String,
    @SerializedName("poster_path") val posterPath: String?
)
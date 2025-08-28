package com.cenkeraydin.composemovie.data.model.movie

import com.cenkeraydin.composemovie.data.model.Genre
import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path")val posterPath: String,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("release_date" )val releaseDate: String,
    @SerializedName("runtime")val runTime: Int,
    @SerializedName("vote_average")val voteAverage: Double,
    @SerializedName("genres") val genres: List<Genre>
)

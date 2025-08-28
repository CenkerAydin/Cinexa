package com.cenkeraydin.composemovie.data.model.movie

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results") val results: List<Movie>
)

data class Movie(
    val id: Int,
    val title: String,
    val originalTitle: String,
    @SerializedName("genre_ids")val genreIds: List<Int>,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("poster_path") val posterPath: String?
)
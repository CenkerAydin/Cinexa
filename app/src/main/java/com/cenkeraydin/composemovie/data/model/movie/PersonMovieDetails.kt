package com.cenkeraydin.composemovie.data.model.movie

import com.google.gson.annotations.SerializedName

data class PersonMovieDetails(
    val id: Int,
    val name: String,
    val biography: String?,
    val birthday: String?,
    @SerializedName("place_of_birth")val placeOfBirth: String?,
    @SerializedName("imdb_id")val imdbId: String?,
    @SerializedName("profile_path")val profilePath: String?
)
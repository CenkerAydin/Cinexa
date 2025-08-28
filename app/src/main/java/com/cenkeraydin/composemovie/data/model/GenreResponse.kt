package com.cenkeraydin.composemovie.data.model

import com.google.gson.annotations.SerializedName

data class Genre(
    val id: Int,
    val name: String
)

data class GenreResponse(
    @SerializedName("genres")
    val genres: List<Genre>
)

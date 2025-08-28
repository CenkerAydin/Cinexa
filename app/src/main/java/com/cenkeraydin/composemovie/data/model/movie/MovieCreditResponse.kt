package com.cenkeraydin.composemovie.data.model.movie

data class MovieCreditsResponse(
    val cast: List<CastMovie>
)

data class CastMovie(
    val id: Int,
    val title: String,
    val character: String?,
    val poster_path: String?,
    val release_date: String?
)
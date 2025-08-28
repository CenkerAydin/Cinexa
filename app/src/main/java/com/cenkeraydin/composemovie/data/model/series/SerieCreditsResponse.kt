package com.cenkeraydin.composemovie.data.model.series


data class SerieCreditsResponse(
    val cast: List<CastSerie>
)

data class CastSerie(
    val id: Int,
    val name: String,
    val character: String?,
    val poster_path: String?,
    val firstAirDate: String?,
)
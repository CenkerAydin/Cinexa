package com.cenkeraydin.composemovie.data.model


data class MultiSearchResponse(
    val page: Int,
    val results: List<SearchResult>,
    val total_pages: Int,
    val total_results: Int
)

data class SearchResult(
    val id: Int,
    val media_type: String,
    val title: String? = null,
    val name: String? = null,
    val overview: String?,
    val poster_path: String?,
    val backdrop_path: String?,
    val release_date: String? = null,
    val first_air_date: String? = null,
    val vote_average: Double?,
    val genre_ids: List<Int>?
)
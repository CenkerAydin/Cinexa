package com.cenkeraydin.composemovie.data.model.person

import com.google.gson.annotations.SerializedName

data class PersonResponse(
    @SerializedName("page")
    val page: Int,

    @SerializedName("results")
    val results: List<Person>,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("total_results")
    val totalResults: Int
)

data class Person(
    @SerializedName("id")
    val id: Int,

    @SerializedName("known_for_department")
    val knownForDepartment: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("original_name")
    val originalName: String?,

    @SerializedName("popularity")
    val popularity: Double,

    @SerializedName("profile_path")
    val profilePath: String?,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("known_for")
    val knownFor: List<KnownForItem>?
)


data class KnownForItem(
    val id: Int,
    val title: String?,
    val name: String?,
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("original_name") val originalName: String?,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("media_type") val mediaType: String
)

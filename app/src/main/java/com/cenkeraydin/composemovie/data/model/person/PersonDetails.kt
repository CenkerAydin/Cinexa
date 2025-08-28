package com.cenkeraydin.composemovie.data.model.person

import com.google.gson.annotations.SerializedName

data class PersonDetails(
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

    @SerializedName("birthday")
    val birthday: String?,

    @SerializedName("biography")
    val biography: String?,

    @SerializedName("place_of_birth")
    val placeOfBirth: String?,


)

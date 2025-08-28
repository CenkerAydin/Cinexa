package com.cenkeraydin.composemovie.data.model.person

import com.google.gson.annotations.SerializedName

data class CombinedCreditsResponse(
    val cast: List<CombinedCreditItem>
)

data class CombinedCreditItem(
    val id: Int,
    val media_type: String,
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("original_name") val originalName: String?,
    val character: String?,
    @SerializedName("poster_path") val posterPath: String?
)
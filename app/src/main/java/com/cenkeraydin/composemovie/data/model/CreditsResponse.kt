package com.cenkeraydin.composemovie.data.model

data class CreditsResponse(
    val id: Int,
    val cast: List<Cast>,
    val crew: List<Crew>
)

data class Cast(
    val id: Int,
    val name: String,
    val character: String,
    val profile_path: String?
){
    val profileImageUrl: String
        get() = if (profile_path != null) {
            "https://image.tmdb.org/t/p/w185$profile_path"
        } else {
            "" // Placeholder for no image
        }
}

data class Crew(
    val id: Int,
    val name: String,
    val job: String,
    val department: String
)

package com.cenkeraydin.composemovie.data.model

data class VideoResponse(
    val results: List<Video>
)

data class Video(
    val name: String,
    val key: String,
    val site: String,
    val type: String,
    val official: Boolean
)
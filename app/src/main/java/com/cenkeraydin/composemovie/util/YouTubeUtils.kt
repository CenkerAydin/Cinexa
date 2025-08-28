package com.cenkeraydin.composemovie.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

fun openYouTube(context: Context, videoKey: String, onError: ((String) -> Unit)? = null) {
    try {
        val youtubeAppIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoKey"))
        youtubeAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (youtubeAppIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(youtubeAppIntent)
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoKey"))
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(browserIntent)
        }
    } catch (e: Exception) {
        Log.e("YouTubeOpen", "YouTube açılamadı", e)
        onError?.invoke("YouTube açılamadı: ${e.localizedMessage}")
    }
}

package com.cenkeraydin.composemovie.util

import android.content.Context
import java.util.Locale

fun Context.setLocale(languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
}
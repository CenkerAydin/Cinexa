package com.cenkeraydin.composemovie.util

import androidx.annotation.StringRes
import com.cenkeraydin.composemovie.R

enum class MovieFilter(@StringRes val displayName: Int) {
    POPULAR(R.string.popular),
    TRENDING(R.string.trends),
    TOP_RATED(R.string.top_rated),
}
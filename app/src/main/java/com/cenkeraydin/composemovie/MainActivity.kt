package com.cenkeraydin.composemovie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.cenkeraydin.composemovie.ui.main.MainScreen
import com.cenkeraydin.composemovie.ui.movie.MovieScreen
import com.cenkeraydin.composemovie.ui.theme.ComposeMovieTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMovieTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen()
                }
            }
        }
    }
}
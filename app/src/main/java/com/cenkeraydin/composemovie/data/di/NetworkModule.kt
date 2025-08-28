package com.cenkeraydin.composemovie.data.di

import com.cenkeraydin.composemovie.data.retrofit.TMDBService
import com.cenkeraydin.composemovie.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideTMDBService(): TMDBService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TMDBService::class.java)
    }
}
package com.cenkeraydin.composemovie.data.di

import android.content.Context
import androidx.room.Room
import com.cenkeraydin.composemovie.data.room.AppDatabase
import com.cenkeraydin.composemovie.data.room.FavoriteMovieDao
import com.cenkeraydin.composemovie.data.room.FavoritePersonDao
import com.cenkeraydin.composemovie.data.room.FavoriteSerieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_db")
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteMovieDao = db.favoriteMovieDao()

    @Provides
    fun provideFavoriteSerieDao(db: AppDatabase): FavoriteSerieDao = db.favoriteSerieDao()

    @Provides
    fun provideFavoritePersonDao(db: AppDatabase): FavoritePersonDao = db.favoritePersonDao()
}

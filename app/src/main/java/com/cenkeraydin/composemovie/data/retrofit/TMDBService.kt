package com.cenkeraydin.composemovie.data.retrofit

import com.cenkeraydin.composemovie.data.model.series.SerieCreditsResponse
import com.cenkeraydin.composemovie.data.model.CreditsResponse
import com.cenkeraydin.composemovie.data.model.GenreResponse
import com.cenkeraydin.composemovie.data.model.movie.MovieCreditsResponse
import com.cenkeraydin.composemovie.data.model.movie.MovieDetailResponse
import com.cenkeraydin.composemovie.data.model.movie.MovieResponse
import com.cenkeraydin.composemovie.data.model.MultiSearchResponse
import com.cenkeraydin.composemovie.data.model.VideoResponse
import com.cenkeraydin.composemovie.data.model.movie.PersonMovieDetails
import com.cenkeraydin.composemovie.data.model.person.CombinedCreditsResponse
import com.cenkeraydin.composemovie.data.model.person.PersonDetails
import com.cenkeraydin.composemovie.data.model.person.PersonResponse
import com.cenkeraydin.composemovie.data.model.series.PersonSerieDetails
import com.cenkeraydin.composemovie.data.model.series.SerieDetailResponse
import com.cenkeraydin.composemovie.data.model.series.SeriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): MovieResponse

    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): MovieResponse

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): GenreResponse

    @GET("genre/tv/list")
    suspend fun getTVGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): GenreResponse

    @GET("discover/movie")
    suspend fun discoverMoviesByGenre(
        @Query("with_genres") genreId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("certification_country") certCountry: String = "US",
        @Query("certification.lte") maxCert: String = "R",
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("discover/tv")
    suspend fun discoverSeriesByGenre(
        @Query("with_genres") genreId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("certification_country") certCountry: String = "US",
        @Query("certification.lte") maxCert: String = "R",
        @Query("page") page: Int = 1
    ): SeriesResponse



    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int ,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US"
    ): MovieResponse

    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int ,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US"
    ): Response<MultiSearchResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): MovieDetailResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<CreditsResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): VideoResponse

    @GET("person/{person_id}")
    suspend fun getPersonMovieDetail(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): Response<PersonMovieDetails>

    @GET("person/{person_id}/movie_credits")
    suspend fun getPersonMovieCredits(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): Response<MovieCreditsResponse>

    @GET("discover/tv")
    suspend fun getTVShows(
        @Query("api_key") apiKey: String,
        @Query("first_air_date.gte") firstAirDateGte: String,
        @Query("sort_by") sortBy: String = "first_air_date.asc",
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
    ): SeriesResponse

    @GET("search/tv")
    suspend fun searchTVShows(
        @Query("query") query: String,
        @Query("first_air_date_year") firstAirDateYear: String = "",
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US"
    ): Response<SeriesResponse>

    @GET("tv/{series_id}")
    suspend fun getTVShowDetail(
        @Path("series_id") seriesId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): SerieDetailResponse

    @GET("tv/{series_id}/credits")
    suspend fun getSerieCredits(
        @Path("series_id") serieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): Response<CreditsResponse>

    @GET("person/{person_id}/tv_credits")
    suspend fun getPersonSerieCredits(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): Response<SerieCreditsResponse>

    @GET("person/{person_id}")
    suspend fun getPersonSerieDetail(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): Response<PersonSerieDetails>

    @GET("trending/tv/{time_window}")
    suspend fun getTrendingTVShows(
        @Path("time_window") timeWindow: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): SeriesResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTVShows(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): SeriesResponse

    @GET("tv/{serie_id}/videos")
    suspend fun getSerieVideos(
        @Path("serie_id") serieId: Int,
        @Query("api_key") apiKey: String,
    ): VideoResponse

    //PERSON
    @GET("person/popular")
    suspend fun getPopularPersons(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<PersonResponse>

    @GET("person/{person_id}")
    suspend fun getPersonDetail(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
    ): Response<PersonDetails>

    @GET("person/{person_id}/combined_credits")
    suspend fun getPersonCombinedCredits(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): Response<CombinedCreditsResponse>

    @GET("trending/person/{time_window}")
    suspend fun getTrendingPerson(
        @Path("time_window") timeWindow: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): PersonResponse

    @GET("search/person")
    suspend fun searchPerson(
        @Query("query") query: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US"
    ): PersonResponse

}
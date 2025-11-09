package com.example.test.api

import retrofit2.http.GET
import retrofit2.http.Query
interface OmdbApiService {

    @GET("?")
    suspend fun searchMovies(
        @Query("s") title: String,
        @Query("apikey") appId: String
    ): OmdbResponse
    @GET("?")
    suspend fun getMovieDetails(
        @Query("i") imdbID: String,
        @Query("apikey") appId: String,): OmdbMovieDetailResponse
}

package com.example.test

import com.example.test.api.OmdbApiService
import com.example.test.api.OmdbMovieDetailResponse
import com.example.test.api.OmdbResponse

class MovieRepository(private val omdbApiService: OmdbApiService) {

    private val API_KEY = "1d981c1f"

    suspend fun searchMovies(movie: String): Result<OmdbResponse> {
        return try {
            val response = omdbApiService.searchMovies(
                title = movie,
                appId = API_KEY,
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMovieDetails(imdbId: String): Result<OmdbMovieDetailResponse>{
        return try {
            val response = omdbApiService.getMovieDetails(
                imdbID = imdbId,
                appId = API_KEY
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
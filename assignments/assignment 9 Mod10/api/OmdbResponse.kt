package com.example.test.api

import com.google.gson.annotations.SerializedName

data class OmdbResponse(
    @SerializedName("Search") val search: List<MovieSearchItem>?,
    @SerializedName("Response") val response : String?,
    @SerializedName("totalResults") val error: String?
)
data class OmdbMovieDetailResponse(
    @SerializedName("Title") val title: String?,
    @SerializedName("Poster") val poster: String?,
    @SerializedName("Year") val year: String?,
    @SerializedName("Rated") val rated: String?,
    @SerializedName("Director") val director: String?,
    @SerializedName("Actors") val actors: String?,
    @SerializedName("Ratings") val ratings: List<Ratings>?,
    @SerializedName("imdbRating") val imdbRating: String?,
    @SerializedName("BoxOffice") val boxOffice: String?,
    @SerializedName("Plot") val plot: String?,
    @SerializedName("Response") val response : String?,
    @SerializedName("Type") val type: String?,
)
data class Ratings(
    @SerializedName("Source") val source: String?,
    @SerializedName("Value") val value: String?
)
data class MovieSearchItem(
    @SerializedName("Title") val title: String?,
    @SerializedName("Year") val year: String?,
    @SerializedName("imdbID") val imdbID: String?,
    @SerializedName("Type") val type: String?,
    @SerializedName("Poster") val poster: String?
)
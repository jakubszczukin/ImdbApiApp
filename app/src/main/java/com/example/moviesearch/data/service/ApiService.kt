package com.example.moviesearch.data.service

import com.example.moviesearch.data.model.Movie
import com.example.moviesearch.data.model.MovieList
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    /**
     *
     * Get top 250 Movies from the Api
     * 
     */
    @GET("en/API/Top250Movies/{apiKey}")
    suspend fun getMovies(@Path("apiKey") apiKey : String) : MovieList

    /**
     *
     * Get detailed info about a title with certain ID
     *
     */
    @GET("en/API/Title/{apiKey}/{id}/Trailer,Ratings,")
    suspend fun getTitleDetailedInfo(@Path("apiKey") apiKey : String, @Path("id") id : String) : Movie
}
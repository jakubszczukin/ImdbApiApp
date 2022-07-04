package com.example.moviesearch.data.repository

import com.example.moviesearch.data.model.Movie
import com.example.moviesearch.data.model.MovieList
import com.example.moviesearch.data.service.ApiService
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Named

class RepositoryImpl @Inject constructor(
    private val apiService : ApiService,
    @Named("apiKey") private val apiKey : String
    ) : Repository{

    override suspend fun getMovies(): MovieList {
        return apiService.getMovies(apiKey)
    }

    override suspend fun getItemDetailedInfo(itemId : String) : Movie {
        return apiService.getTitleDetailedInfo(apiKey, itemId)
    }
}
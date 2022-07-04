package com.example.moviesearch.data.repository

import com.example.moviesearch.data.model.Movie
import com.example.moviesearch.data.model.MovieList

interface Repository {
    suspend fun getMovies() : MovieList
    suspend fun getItemDetailedInfo(itemId : String) : Movie
}
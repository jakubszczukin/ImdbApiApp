package com.example.moviesearch.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesearch.data.model.Movie
import com.example.moviesearch.data.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repositoryImpl : RepositoryImpl) : ViewModel() {
    var movieListResponse: List<Movie> by mutableStateOf(listOf())
    private var errorMessage: String by mutableStateOf("")

    fun getMovieList(){
        viewModelScope.launch{
            if(movieListResponse.isEmpty()){
                try{
                    val movieList = repositoryImpl.getMovies().items
                    movieListResponse = movieList
                } catch(e : Exception){
                    errorMessage = e.message.toString()
                    Log.d("ErrorMessage", errorMessage)
                }
            }
        }
    }

}
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
class DetailsViewModel @Inject constructor(private val repositoryImpl: RepositoryImpl) : ViewModel() {
    var movie : Movie? by mutableStateOf(null)
    private var errorMessage: String by mutableStateOf("")

    fun getItemById(itemId : String){
        viewModelScope.launch{
            try{
                val item = repositoryImpl.getItemDetailedInfo(itemId)
                movie = item
            }catch(e : Exception){
                errorMessage = e.message.toString()
                Log.d("errorMessage", errorMessage)
            }
        }
    }
}
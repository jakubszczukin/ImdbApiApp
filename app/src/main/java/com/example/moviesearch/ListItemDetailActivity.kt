package com.example.moviesearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moviesearch.ui.ItemDetailScreen
import com.example.moviesearch.ui.theme.ImdbApiAppTheme
import com.example.moviesearch.ui.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListItemDetailActivity : ComponentActivity() {

    private val movieId : String by lazy{
        intent?.getSerializableExtra("itemId") as String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val detailsViewModel = hiltViewModel<DetailsViewModel>()
            ImdbApiAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val movie = detailsViewModel.movie
                    detailsViewModel.getItemById(movieId)
                    ItemDetailScreen(movie)
                }
            }
        }
    }

}

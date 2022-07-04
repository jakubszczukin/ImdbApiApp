package com.example.moviesearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.moviesearch.ui.nav.BottomNavigationScreen
import com.example.moviesearch.ui.nav.MainScreenNavigationConf
import com.example.moviesearch.ui.nav.NavigationBottomBar
import com.example.moviesearch.ui.theme.ImdbApiAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImdbApiAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(){

    val navController = rememberNavController()

    val bottomNavigationItems = listOf(
        BottomNavigationScreen.TopMovies,
        BottomNavigationScreen.TopTvShows,
        BottomNavigationScreen.Search
    )

    Scaffold(
        bottomBar = {
            NavigationBottomBar(navController, bottomNavigationItems)
        },
    ){
        MainScreenNavigationConf(navController)
    }
}

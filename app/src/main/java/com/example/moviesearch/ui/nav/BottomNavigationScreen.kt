package com.example.moviesearch.ui.nav

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.moviesearch.R

sealed class BottomNavigationScreen(
    val route: String,
    @StringRes val resourceId : Int,
    val icon: ImageVector
    ){
    object TopMovies : BottomNavigationScreen("Top Movies", R.string.TopMovies, Icons.Outlined.Movie)
    object TopTvShows : BottomNavigationScreen("Top Tv Shows", R.string.TopTvShows, Icons.Outlined.Tv)
    object Search : BottomNavigationScreen("Search", R.string.Search, Icons.Outlined.Search)
}
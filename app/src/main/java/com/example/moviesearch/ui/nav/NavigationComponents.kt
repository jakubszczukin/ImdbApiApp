package com.example.moviesearch.ui.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.moviesearch.ui.MovieListScreen
import com.example.moviesearch.ui.viewmodel.MainViewModel


@Composable
fun MainScreenNavigationConf(navController : NavHostController){
    val mainViewModel = hiltViewModel<MainViewModel>()
    NavHost(navController, startDestination = BottomNavigationScreen.TopMovies.route){
        composable(BottomNavigationScreen.TopMovies.route){
            mainViewModel.getMovieList()
            if(mainViewModel.movieListResponse.isEmpty()){
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            } else{
                MovieListScreen(movieList = mainViewModel.movieListResponse)
            }
        }
        composable(BottomNavigationScreen.TopTvShows.route){
            Text("Test - TV SHOWS")
        }
        composable(BottomNavigationScreen.Search.route){
            Text("Test - SEARCH")
        }
    }
}

@Composable
fun NavigationBottomBar(navController: NavHostController, items: List<BottomNavigationScreen>){

    NavigationBar{
        // get current navigation route
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach{item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.route) },
                selected = currentDestination?.hierarchy?.any {it.route == item.route} == true,
                alwaysShowLabel = false, // this hides the title for the unselected items
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                   }
                }
            )
        }
    }
}
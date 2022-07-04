package com.example.moviesearch.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.moviesearch.ListItemDetailActivity
import com.example.moviesearch.data.model.Movie

@Composable
fun MovieListScreen(movieList : List<Movie>){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Top 250 ImDb Movies of All Time",
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        MovieList(movieList)
    }
}


/**
 *
 * Lazy Column List for popular movies
 *
 */
@Composable
fun MovieList(movieList : List<Movie>){
    val context = LocalContext.current

    LazyColumn{
        itemsIndexed(movieList){ _, item ->
            MovieItem(item, onClick = {
                val intent = Intent(context, ListItemDetailActivity::class.java)
                intent.putExtra("itemId", item.id)
                context.startActivity(intent)
            })
        }
    }
}

/**
 *
 * Single Item Row Composable for the Lazy Column List
 *
 **/
@Composable
fun MovieItem(movie: Movie, onClick: (Movie) -> Unit){
    Card(
        modifier = Modifier
            .padding(6.dp, 6.dp)
            .fillMaxWidth()
            .clickable {
                onClick(movie)
            }
            .height(110.dp),
        shape = RoundedCornerShape(6.dp),
        elevation = 4.dp
    ){
        Surface{
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxSize()
            ){
                AsyncImage(
                    model = movie.image,
                    contentDescription = "Idk something here",
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.2f)
                )

                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxHeight()
                        .weight(0.8f)
                ){
                    Text(
                        text = movie.title + " (" + movie.rank + ")",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.padding(top = 10.dp))
                    Text(
                        text = movie.crew,
                        style = MaterialTheme.typography.body1,
                    )

                }
            }
        }
    }
}

package com.example.moviesearch.ui

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.moviesearch.R
import com.example.moviesearch.data.model.Movie
import com.example.moviesearch.data.model.Person
import com.example.moviesearch.data.model.Trailer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView

@Composable
fun ItemDetailScreen(item: Movie?){
    if(item == null){
        Box(Modifier.fillMaxSize(), contentAlignment = Center){
            CircularProgressIndicator()
        }
    }
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ){
            /**
             * Column for image and texts under it
             */
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = CenterHorizontally
            ){
                /**
                 * Image card
                 */
                Card(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .height(480.dp)
                ){
                    AsyncImage(
                        model = item?.image,
                        contentDescription = item?.plot,
                        contentScale = ContentScale.Inside,
                        placeholder = painterResource(R.drawable.placeholder_image),
                        modifier = Modifier.clip(RoundedCornerShape(14.dp))
                    )
                }

                /**
                 * Full title
                 */
                Text(
                    text = if(item?.fullTitle != null) item.fullTitle else "Not available",
                    modifier = Modifier.padding(2.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5
                )

                /**
                 * Series of texts right under the full title
                 */
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, end = 40.dp),
                    horizontalArrangement = Arrangement.Center
                ){

                    TwoTextsInRow(string1 = "Year: ", string2 = item?.year.toString())

                    TwoTextsInRow(string1 = "Rating: ", string2 = item?.imDbRating.toString())

                    TwoTextsInRow(string1 = "Votes: ", string2 = item?.imDbRatingVotes.toString())

                }

                TwoTextsInRow(string1 = "Genres: ", string2 = item?.genres.toString())

                Text(
                    text = if(item?.plot != null && item.plot != "") item.plot else "Not available",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
                    textAlign = TextAlign.Justify
                )
            }

            /**
             * Actors lazy row
             */
            PersonLazyList(item?.actorList)

            /**
             * Trailer video player
             */
            VideoPlayer(Uri.parse(item?.trailer?.linkEmbed))
        }
    }
}

@Composable
fun VideoPlayer(uri : Uri){
    val context = LocalContext.current

    // Do not recreate the player whenever thic Composable commits
    val exoPlayer = remember(context){
        ExoPlayer.Builder(context).build().apply{
            val mediaItem = MediaItem.Builder()
                .setUri(uri)
                .build()
            setMediaItem(mediaItem)
            prepare()
        }
    }

    AndroidView(factory = { mContext ->
        StyledPlayerView(mContext).apply{
            player = exoPlayer
        }
    })
}

@Composable
fun PersonLazyList(personList : List<Person>?){
    if(personList != null){
        LazyRow{
            items(personList){ person ->
                ActorListItem(person)
            }
        }
    }
}

@Composable
fun ActorListItem(actor : Person){
    Card{
        Column(
            horizontalAlignment = CenterHorizontally
        ){
            Card(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .height(180.dp)
                    .width(120.dp)
            ){
                AsyncImage(
                    model = actor.image,
                    contentDescription = actor.name,
                    contentScale = ContentScale.FillBounds,
                    placeholder = painterResource(R.drawable.placeholder_image),
                    modifier = Modifier.clip(RoundedCornerShape(14.dp))
                )
            }
            Text(
                text = actor.name,
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = actor.asCharacter,
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Composable
fun TwoTextsInRow(string1 : String, string2 : String){
    Row(){
        Text(
            text = string1,
            style = MaterialTheme.typography.subtitle2
        )

        Text(
            modifier = Modifier.padding(end = 4.dp),
            text = if(string2 != "" && string2 != "null") string2 else "Not available",
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ActorListItemPreview(){
    val person = Person(
        id = "nm0000138",
        image = "https://imdb-api.com/images/original/MV5BMjI0MTg3MzI0M15BMl5BanBnXkFtZTcwMzQyODU2Mw@@._V1_Ratio1.0000_AL_.jpg",
        name = "Leonardo DiCaprio",
        asCharacter = "Cobbas Cobb"
    )
    ActorListItem(person)
}

@Composable
@Preview(showBackground = true)
fun ItemDetailScreenPreview(){
    val movie = Movie(
        id = "tt1375666",
        title = "Inception",
        fullTitle = "Inception (2010)",
        year = 2010,
        image = "https://imdb-api.com/images/original/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_Ratio0.6751_AL_.jpg",
        releaseDate = "2010-07-16",
        imDbRating = 9.4f,
        imDbRatingVotes = 123545,
        crew = "asdasd",
        rank = 4,
        plot = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster.",
        genres = "Horror, Action, Thriller",
        trailer = Trailer(
            imDbId = "tt1375666",
            title = "Inception",
            fullTitle = "Inception (2010)",
            type = "Movie",
            year = 2010,
            videoId = "vi2959588889",
            videoTitle = "10th Anniversary Dream Trailer",
            videoDescription = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.\r\n\r\n",
            thumbnailUrl = "https://m.media-amazon.com/images/M/MV5BMTQ1ZmIzOTAtNDcwZi00NDVkLWE4NWItYWNhZGY1MmVlZGU0XkEyXkFqcGdeQWRvb2xpbmhk._V1_.jpg",
            link = "https://www.imdb.com/video/vi2959588889",
            linkEmbed = "https://www.imdb.com/video/imdb/vi2959588889/imdb/embed",
            errorMessage = ""
        ),
        actorList = listOf(
            Person(
                id = "nm0000138",
                image = "https://imdb-api.com/images/original/MV5BMjI0MTg3MzI0M15BMl5BanBnXkFtZTcwMzQyODU2Mw@@._V1_Ratio1.0000_AL_.jpg",
                name = "Leonardo DiCaprio",
                asCharacter = "Cobbas Cobb"
            ),
            Person(
                id = "nm0000138",
                image = "https://imdb-api.com/images/original/MV5BMjI0MTg3MzI0M15BMl5BanBnXkFtZTcwMzQyODU2Mw@@._V1_Ratio1.0000_AL_.jpg",
                name = "Leonardo DiCaprio",
                asCharacter = "Cobbas Cobb"
            ),
            Person(
                id = "nm0000138",
                image = "https://imdb-api.com/images/original/MV5BMjI0MTg3MzI0M15BMl5BanBnXkFtZTcwMzQyODU2Mw@@._V1_Ratio1.0000_AL_.jpg",
                name = "Leonardo DiCaprio",
                asCharacter = "Cobbas Cobb"
            ),
            Person(
                id = "nm0000138",
                image = "https://imdb-api.com/images/original/MV5BMjI0MTg3MzI0M15BMl5BanBnXkFtZTcwMzQyODU2Mw@@._V1_Ratio1.0000_AL_.jpg",
                name = "Leonardo DiCaprio",
                asCharacter = "Cobbas Cobb"
            ),
            Person(
                id = "nm0000138",
                image = "https://imdb-api.com/images/original/MV5BMjI0MTg3MzI0M15BMl5BanBnXkFtZTcwMzQyODU2Mw@@._V1_Ratio1.0000_AL_.jpg",
                name = "Leonardo DiCaprio",
                asCharacter = "Cobbas Cobb"
            ),
        )
    )

    ItemDetailScreen(item = movie)
}

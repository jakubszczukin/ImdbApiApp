package com.example.moviesearch.ui

import android.content.Intent
import android.net.Uri
import android.text.SpannableString
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.moviesearch.ListItemDetailActivity
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
                        model = item.image,
                        contentDescription = item.plot,
                        contentScale = ContentScale.Inside,
                        placeholder = painterResource(R.drawable.placeholder_image),
                        modifier = Modifier.clip(RoundedCornerShape(14.dp))
                    )
                }

                /**
                 * Full title
                 */
                Text(
                    text = item.fullTitle,
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

                    TwoTextsInRow(string1 = "Year: ", string2 = item.year.toString())

                    TwoTextsInRow(string1 = "Rating: ", string2 = item.imDbRating.toString())

                    TwoTextsInRow(string1 = "Votes: ", string2 = item.imDbRatingVotes.toString())

                }

                TwoTextsInRow(string1 = "Genres: ", string2 = item.genres)

                Text(
                    text = if(item.plot != "") item.plot else "Not available",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
                    textAlign = TextAlign.Justify
                )
            }

            /**
             * Director list info
             */
            Spacer(modifier = Modifier.weight(1f))
            StaffListInfo("Director", item.directorList)

            /**
             * Writer list info
             */
            Spacer(modifier = Modifier.weight(1f))
            StaffListInfo("Writers", item.writerList)

            /**
             * Star list info
             */
            Spacer(modifier = Modifier.weight(1f))
            StaffListInfo("Stars", item.starList)

            /**
             * Actors lazy row
             */
            Text(
                text = "Top cast >",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp)
            )
            PersonLazyList(item.actorList)

            /**
             * Similar titles
             */
            Text(
                text = "More like this >",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp)
            )
            SimilarTitlesLazyList(titleList = item.similars)

            /**
             * Trailer video player
             */
            //VideoPlayer(Uri.parse(item.trailer?.linkEmbed))
        }
    }
}

@Composable
fun StaffListInfo(position : String, personList : List<Person>){
    if(personList.isNotEmpty()){
        Row{
            Text(
                text = position,
                modifier = Modifier.padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp),
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                buildAnnotatedString {
                    withStyle(
                        ParagraphStyle(
                        lineHeight = 20.sp
                    )
                    ){
                        withStyle(SpanStyle(
                            fontFamily = MaterialTheme.typography.body2.fontFamily,
                        )){
                            personList.forEachIndexed { index, person ->
                                if(index > 0)
                                    append("\u2022  " + person.name + "  ")
                                else
                                    append(person.name + "  ")
                            }
                        }
                    }
                },
                modifier = Modifier.padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp)
            )
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
fun SimilarTitlesLazyList(titleList : List<Movie>){
    val context = LocalContext.current

    if(titleList.isNotEmpty()){
        LazyRow{
            items(titleList){ title ->
                TitleListItem(title, onClick = {
                    val intent = Intent(context, ListItemDetailActivity::class.java)
                    intent.putExtra("itemId", title.id)
                    context.startActivity(intent)
                })
            }
        }
    }
}

@Composable
fun TitleListItem(movie : Movie, onClick : (Movie) -> Unit){
    Card{
        Column(
            horizontalAlignment = CenterHorizontally
        ){
            Card(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .height(200.dp)
                    .width(150.dp)
                    .clickable{onClick(movie)}
            ){
                AsyncImage(
                    model = movie.image,
                    contentDescription = movie.title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.clip(RoundedCornerShape(4.dp)),
                )
            }
            Text(
                text = movie.title,
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = ("Rating: " + movie.imDbRating),
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
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
fun PersonListItem(person : Person){
    Card{
        Column(
            horizontalAlignment = CenterHorizontally
        ){
            Card(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .height(120.dp)
                    .width(100.dp)
            ){
                AsyncImage(
                    model = person.image,
                    contentDescription = person.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(RoundedCornerShape(4.dp)),
                )
            }
            Text(
                text = person.name,
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Composable
fun ActorListItem(actor : Person){
    Column(
        horizontalAlignment = CenterHorizontally
    ){
        PersonListItem(person = actor)
        Text(
            text = actor.asCharacter,
            style = MaterialTheme.typography.caption
        )
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
        ),
        directorList = listOf(
            Person(
                id = "nm0000138",
                image = "https://imdb-api.com/images/original/MV5BMjI0MTg3MzI0M15BMl5BanBnXkFtZTcwMzQyODU2Mw@@._V1_Ratio1.0000_AL_.jpg",
                name = "Leonardo DiCaprio",
                asCharacter = "Cobbas Cobb"
            )
        ),
        writerList = listOf(
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
            )
        ),
        starList = listOf(
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
            )
        ),
        similars = listOf(
            Movie(
                id = "tt0816692",
                title = "Interstellar",
                image = "https://imdb-api.com/images/original/MV5BZjdkOTU3MDktN2IxOS00OGEyLWFmMjktY2FiMmZkNWIyODZiXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_Ratio0.6763_AL_.jpg",
                imDbRating = 8.6f
            ),
            Movie(
                id = "tt0816692",
                title = "Fight Club",
                image = "https://imdb-api.com/images/original/MV5BZjdkOTU3MDktN2IxOS00OGEyLWFmMjktY2FiMmZkNWIyODZiXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_Ratio0.6763_AL_.jpg",
                imDbRating = 8.6f
            ),
            Movie(
                id = "tt0816692",
                title = "The Dark Knight",
                image = "https://imdb-api.com/images/original/MV5BZjdkOTU3MDktN2IxOS00OGEyLWFmMjktY2FiMmZkNWIyODZiXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_Ratio0.6763_AL_.jpg",
                imDbRating = 8.6f
            )
        )
    )

    ItemDetailScreen(item = movie)
}

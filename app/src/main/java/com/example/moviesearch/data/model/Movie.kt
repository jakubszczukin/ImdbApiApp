package com.example.moviesearch.data.model

import java.io.Serializable

data class Movie(
    // Top 250 Movie Api call
    val id: String = "",
    val rank: Int = 0, // is not in detailed search
    val title: String = "",
    val fullTitle: String = "",
    val year: Int = 0,
    val image: String = "",
    val crew : String = "", // is not in detailed search
    val imDbRating : Float = 0f,
    val imDbRatingCount : Int = -1, // is not in detailed search

    // Additional stuff (detailed search)
    val imDbRatingVotes : Int = -1,
    val releaseDate : String = "",
    val runtimeMins : String = "",
    val runtimeStr : String = "",
    val plot : String = "",
    val awards : String = "",
    val directorList : List<Person> = listOf(),
    val writerList : List<Person> = listOf(),
    val starList : List<Person> = listOf(),
    val actorList : List<Person> = listOf(),
    val genres : String = "",
    val companies : String = "",
    val countries : String = "",
    val languages : String = "",
    val trailer : Trailer? = null,
    val similars : List<Movie> = listOf()
) : Serializable
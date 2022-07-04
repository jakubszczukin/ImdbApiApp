package com.example.moviesearch.data.model

import android.net.Uri

data class Trailer(
    val imDbId : String,
    val title : String,
    val fullTitle : String,
    val type : String,
    val year : Int,
    val videoId : String,
    val videoTitle : String,
    val videoDescription : String,
    val thumbnailUrl : String,
    val link : String,
    val linkEmbed : String,
    val errorMessage : String
)

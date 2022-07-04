package com.example.moviesearch.data.di

import com.example.moviesearch.BuildConfig
import com.example.moviesearch.data.repository.RepositoryImpl
import com.example.moviesearch.data.service.ApiService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val BASE_URL = "https://imdb-api.com/"

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    @Named("apiKey")
    fun provideApiKey() : String {
        return BuildConfig.API_KEY
    }

    @Singleton
    @Provides
    fun provideRepository(apiService: ApiService, @Named("apiKey") apiKey: String) : RepositoryImpl {
        return RepositoryImpl(apiService, apiKey)
    }

}
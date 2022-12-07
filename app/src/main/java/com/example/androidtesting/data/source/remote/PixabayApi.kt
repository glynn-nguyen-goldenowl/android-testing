package com.example.androidtesting.data.source.remote

import com.example.androidtesting.data.source.remote.dto.ImageResponseDto
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {
    @GET("/api/")
    suspend fun searchImage(
        @Query("q") queryString: String,
        @Query("key") apiKey: String
    ): Response<ImageResponseDto>
}
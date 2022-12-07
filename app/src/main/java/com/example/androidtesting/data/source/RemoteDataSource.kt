package com.example.androidtesting.data.source

import com.example.androidtesting.data.DataResult
import com.example.androidtesting.data.source.remote.dto.ImageResultDto
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun searchImage(query: String): Flow<DataResult<List<ImageResultDto>>>
}
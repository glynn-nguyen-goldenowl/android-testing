package com.example.androidtesting.data.source.remote

import com.example.androidtesting.BuildConfig
import com.example.androidtesting.data.DataResult
import com.example.androidtesting.data.source.RemoteDataSource
import com.example.androidtesting.data.source.remote.dto.ImageResultDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DefaultRemoteDataSource(val pixabayApi: PixabayApi) : RemoteDataSource {
    override suspend fun searchImage(query: String): Flow<DataResult<List<ImageResultDto>>> {
      return flow{
            val response = pixabayApi.searchImage(query, BuildConfig.API_KEY)
            if(response.isSuccessful && response.body() !=null){
                emit(DataResult.Success(response.body()!!.hits))
            }else{
                emit(DataResult.Error(Throwable("An error occurred")))
            }
        }
    }
}
package com.example.androidtesting.data.source

import com.example.androidtesting.data.DataResult
import com.example.androidtesting.data.source.local.ShoppingItem
import com.example.androidtesting.data.source.remote.ImageResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultShoppingRepository(
    val localDataSource: LocalDataSource,
    val remoteDataSource: RemoteDataSource,
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        withContext(ioDispatcher){
            localDataSource.insertShoppingItem(shoppingItem)
        }
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        withContext(ioDispatcher){
            localDataSource.deleteShoppingItem(shoppingItem)
        }
    }

    override suspend fun updateAmountShoppingItem(shoppingItem: ShoppingItem, amount: Int) {
        withContext(ioDispatcher){
            localDataSource.updateAmountShoppingItem(shoppingItem, amount)
        }
    }

    override suspend fun searchImage(queryString: String): Flow<DataResult<List<ImageResult>>> {
        return remoteDataSource.searchImage(queryString).map {
            when(it){
                is DataResult.Success ->{
                    val dataList = it.data.map { image ->
                        ImageResult(image.id, image.previewURL)
                    }
                    DataResult.Success(dataList)
                }
                is DataResult.Error ->{
                    DataResult.Error(it.error)
                }
            }
        }
    }

    override fun observeAllShoppingItem(): Flow<DataResult<List<ShoppingItem>>> {
        return localDataSource.observeAllShoppingItem()
    }

    override fun observeTotalPrice(): Flow<DataResult<Int>> {
        return localDataSource.observeTotalPrice()
    }
}
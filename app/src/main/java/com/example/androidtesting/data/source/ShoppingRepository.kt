package com.example.androidtesting.data.source

import com.example.androidtesting.data.DataResult
import com.example.androidtesting.data.source.local.ShoppingItem
import com.example.androidtesting.data.source.remote.ImageResult
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)
    suspend fun updateAmountShoppingItem(shoppingItem: ShoppingItem, amount: Int)
    suspend fun searchImage(queryString: String): Flow<DataResult<List<ImageResult>>>
    fun observeAllShoppingItem(): Flow<DataResult<List<ShoppingItem>>>
    fun observeTotalPrice(): Flow<DataResult<Int>>
}
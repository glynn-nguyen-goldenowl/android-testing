package com.example.androidtesting.data.source

import com.example.androidtesting.data.DataResult
import com.example.androidtesting.data.source.local.ShoppingItem
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)
    fun observeAllShoppingItem(): Flow<DataResult<List<ShoppingItem>>>
    fun observeTotalPrice(): Flow<DataResult<Int>>
}
package com.example.androidtesting.data.source.local

import android.util.Log
import com.example.androidtesting.data.DataResult
import com.example.androidtesting.data.source.LocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultLocalDataSource(
    val shoppingDao: ShoppingDao
) : LocalDataSource {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override suspend fun updateAmountShoppingItem(shoppingItem: ShoppingItem, amount: Int) {
        shoppingDao.updateAmountShoppingItem(shoppingItem.id ?: -1, amount)
    }

    override fun observeAllShoppingItem(): Flow<DataResult<List<ShoppingItem>>> {
        return shoppingDao.observeAllShoppingItem().map {
            DataResult.Success(it)
        }
    }

    override fun observeTotalPrice(): Flow<DataResult<Int>> {
        return shoppingDao.observeTotalPrice().map {
            DataResult.Success(it ?: 0)
        }
    }

}
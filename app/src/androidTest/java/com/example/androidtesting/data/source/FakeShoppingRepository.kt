package com.example.androidtesting.data.source

import com.example.androidtesting.data.DataResult
import com.example.androidtesting.data.source.local.ShoppingItem
import com.example.androidtesting.data.source.remote.ImageResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeShoppingRepository : ShoppingRepository {

    val shoppingItemList = mutableListOf<ShoppingItem>()
    var shouldImageSearchError: Boolean = false


    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItemList.add(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItemList.remove(shoppingItem)
    }

    override suspend fun searchImage(queryString: String): Flow<DataResult<List<ImageResult>>> {
        val imageList = listOf(ImageResult(1, "https://pixabay.com?id=1"), ImageResult(2, "https://pixabay.com?id=2"))
        return if(!shouldImageSearchError){
            flowOf(
                DataResult.Success(imageList)
            )
        }else{
            flowOf(
                DataResult.Error(Throwable("\"An error occurred\""))
            )
        }

    }

    override fun observeAllShoppingItem(): Flow<DataResult<List<ShoppingItem>>> {
        return flowOf(DataResult.Success(shoppingItemList))
    }

    override fun observeTotalPrice(): Flow<DataResult<Int>> {
        val totalPrice = shoppingItemList.sumOf { it.price * it.amount }
        return flowOf(DataResult.Success(totalPrice))
    }
}
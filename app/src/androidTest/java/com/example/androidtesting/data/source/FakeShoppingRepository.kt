package com.example.androidtesting.data.source

import com.example.androidtesting.data.DataResult
import com.example.androidtesting.data.source.local.ShoppingItem
import com.example.androidtesting.data.source.remote.ImageResult
import com.example.androidtesting.util.Async
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FakeShoppingRepository : ShoppingRepository {

    var shouldImageSearchError: Boolean = false

    val totalPriceFlow : MutableStateFlow<Int> = MutableStateFlow(0)
    val shoppingItemFlow : MutableStateFlow<MutableList<ShoppingItem>> = MutableStateFlow(mutableListOf())

    private var  shouldReturnShoppingItemError = false

    fun setReturnShoppingItemError(value: Boolean) {
        shouldReturnShoppingItemError = value
    }

    private var  shouldReturnTotalPriceError = false

    fun setReturnTotalPriceError(value: Boolean) {
        shouldReturnTotalPriceError = value
    }


    private val observableShoppingItems: Flow<DataResult<List<ShoppingItem>>> = shoppingItemFlow.map {
        if (shouldReturnShoppingItemError) {
            DataResult.Error(Throwable("An error occurred"))
        } else {
            DataResult.Success(it)
        }
    }

    private val observableTotalPrice: Flow<DataResult<Int>> = totalPriceFlow.map {
        if (shouldReturnTotalPriceError) {
            DataResult.Error(Throwable("An error occurred"))
        } else {
            DataResult.Success(it)
        }
    }


    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        val items = shoppingItemFlow.value
        items.add(shoppingItem)
        shoppingItemFlow.value = items
        val totalPrice = items.sumOf { it.price * it.amount }
        totalPriceFlow.emit(totalPrice)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        val items = shoppingItemFlow.value
        items.remove(shoppingItem)
        shoppingItemFlow.value = items
        val totalPrice = items.sumOf { it.price * it.amount }
        totalPriceFlow.emit(totalPrice)

    }

    override suspend fun updateAmountShoppingItem(shoppingItem: ShoppingItem, amount: Int) {

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
        return observableShoppingItems
    }

    override fun observeTotalPrice(): Flow<DataResult<Int>> {
        return observableTotalPrice
    }
}
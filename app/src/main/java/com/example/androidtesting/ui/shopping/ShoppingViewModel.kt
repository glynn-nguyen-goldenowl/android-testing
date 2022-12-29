package com.example.androidtesting.ui.shopping


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtesting.data.DataResult
import com.example.androidtesting.data.source.ShoppingRepository
import com.example.androidtesting.data.source.local.ShoppingItem
import com.example.androidtesting.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    val shoppingRepository: ShoppingRepository
): ViewModel(){

    private val _errorMessage : MutableStateFlow<String?> = MutableStateFlow(null)

    private val _shoppingItemListAsync = shoppingRepository.observeAllShoppingItem()
        .map {
            Async.Success(it)
         }
        .onStart<Async<DataResult<List<ShoppingItem>>>> { emit(Async.Loading) }
        .onEach {
            if(it is Async.Success && it.data is DataResult.Error){
                _errorMessage.value = it.data.error.message
            }
        }

    private val _totalPriceAsync = shoppingRepository.observeTotalPrice()
        .map {
            Async.Success(it)
        }
        .onStart<Async<DataResult<Int>>> { emit(Async.Loading) }
        .onEach {
            if(it is Async.Success && it.data is DataResult.Error){
                _errorMessage.value = it.data.error.message
            }
        }

    val uiState: StateFlow<ShoppingUiState> = combine(
        _shoppingItemListAsync, _totalPriceAsync, _errorMessage
    ){ itemList, totalPrice, errorMessage ->
        mergeState(itemList, totalPrice, errorMessage)
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ShoppingUiState(loading = true)
    )

    private fun mergeState(
        itemList: Async<DataResult<List<ShoppingItem>>>,
        totalPrice: Async<DataResult<Int>>,
        errorMessage: String?
    ):ShoppingUiState {
        val isLoading = (itemList == Async.Loading) || (totalPrice == Async.Loading)
        val shoppingItemList = if(itemList is Async.Success && itemList.data is DataResult.Success) itemList.data.data else listOf()
        val price = if(totalPrice is Async.Success && totalPrice.data is DataResult.Success) totalPrice.data.data else -1
        return ShoppingUiState(shoppingItemList, price, isLoading, errorMessage)
    }


    fun onEvent(event: ShoppingUiEvent){
        when(event){
            is ShoppingUiEvent.DeleteItem ->{
                deleteShoppingItem(event.item)
            }
            is ShoppingUiEvent.InsertItem ->{
                insertShoppingItem(event.item)
            }
            is ShoppingUiEvent.ConsumeError ->{
                consumeErrorMessage(event.error)
            }
        }
    }

    private fun consumeErrorMessage(error: String) {
        viewModelScope.launch {
            _errorMessage.value = null
        }
    }

    private fun insertShoppingItem(item: ShoppingItem) {
        viewModelScope.launch {
            shoppingRepository.insertShoppingItem(shoppingItem = item)
        }
    }

    private fun deleteShoppingItem(item: ShoppingItem) {
        viewModelScope.launch {
            shoppingRepository.deleteShoppingItem(shoppingItem = item)
        }
    }

}

package com.example.androidtesting.ui.shopping

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtesting.data.DataResult
import com.example.androidtesting.data.source.ShoppingRepository
import com.example.androidtesting.data.source.local.ShoppingItem
import com.example.androidtesting.ui.add.AddShoppingItemUiState
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

    val uiState: StateFlow<ShoppingUiState> = combine(
        _shoppingItemListAsync, shoppingRepository.observeTotalPrice(), _errorMessage
    ){ itemList, totalPrice, errorMessage ->
        when(itemList){
            Async.Loading ->{
                ShoppingUiState(listOf(), 0, loading = true, errorMessage = errorMessage)
            }
            is Async.Success -> {
                if (itemList.data is DataResult.Success) {
                    ShoppingUiState(
                        itemList.data.data,
                        (totalPrice as DataResult.Success).data,
                        loading = false,
                        errorMessage = errorMessage
                    )
                } else {
                    ShoppingUiState(
                        listOf(),
                        (totalPrice as DataResult.Success).data,
                        loading = false,
                        errorMessage = errorMessage
                    )
                }

            }
        }

    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ShoppingUiState(loading = true)
    )

    fun onEvent(event: ShoppingUiEvent){
        when(event){
            is ShoppingUiEvent.DeleteItem ->{
                deleteShoppingItem(event.item)
            }
            is ShoppingUiEvent.InsertItem ->{
                insertShoppingItem(event.item)
            }
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

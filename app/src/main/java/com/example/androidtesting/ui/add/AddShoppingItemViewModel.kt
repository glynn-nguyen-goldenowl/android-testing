package com.example.androidtesting.ui.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtesting.data.source.ShoppingRepository
import com.example.androidtesting.data.source.local.ShoppingItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddShoppingItemViewModel @Inject constructor(
    val shoppingRepository: ShoppingRepository,
    val stateHandle: SavedStateHandle
) : ViewModel() {

    private val _imageUrlState: MutableStateFlow<String?> =
        MutableStateFlow(null)
    val imageUrlState = _imageUrlState.asStateFlow()

    private val _uiState: MutableStateFlow<AddShoppingItemUiState> =
        MutableStateFlow(AddShoppingItemUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: AddShoppingItemUiEvent) {
        when (event) {
            is AddShoppingItemUiEvent.AddItem -> {
                addShoppingItem(event.name, event.amount, event.price)
            }
            is AddShoppingItemUiEvent.SetImageUrl ->{
                setImageUrl(event.imageUrl)
            }
        }
    }

    private fun setImageUrl(imageUrl: String) {
        viewModelScope.launch {
            _imageUrlState.emit(imageUrl)
            if(imageUrl.isNotEmpty() && _uiState.value.errorMessage == INVALID_IMAGE_URL){
                _uiState.emit(AddShoppingItemUiState())
            }
        }
    }

    private fun validateUserInput(name: String, amount: String, price: String, imageUrl: String): String {
        if (name.isEmpty()) {
            return INVALID_NAME
        }
        if (amount.isEmpty() || amount.toInt() <= 0) {
            return INVALID_AMOUNT
        }
        if (price.isEmpty() || amount.toInt() <= 0) {
            return INVALID_PRICE
        }
        if(imageUrl.isEmpty())
            return INVALID_IMAGE_URL
        return VALID
    }

    private fun addShoppingItem(name: String, amount: String, price: String) {
        viewModelScope.launch {
            val validateResult = validateUserInput(name, amount, price,  _imageUrlState.value ?: "")
            if(validateResult == VALID){
                val shoppingItem = ShoppingItem(name, amount.toInt(), price.toInt(), _imageUrlState.value!!)
                shoppingRepository.insertShoppingItem(shoppingItem)
                _uiState.emit(AddShoppingItemUiState(shoppingItem = shoppingItem))
            }else{
                _uiState.emit(AddShoppingItemUiState(errorMessage = validateResult))
            }
        }
    }

    companion object {
        const val VALID = "valid"
        const val INVALID_NAME = "invalid_name"
        const val INVALID_AMOUNT = "invalid_amount"
        const val INVALID_PRICE = "invalid_price"
        const val INVALID_IMAGE_URL = "invalid_image_url"
    }


}
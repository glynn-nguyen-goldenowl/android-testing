package com.example.androidtesting.ui.add

sealed class AddShoppingItemUiEvent {
    data class AddItem(val name: String, val amount: String, val price: String): AddShoppingItemUiEvent()
    data class SetImageUrl(val imageUrl: String): AddShoppingItemUiEvent()
}
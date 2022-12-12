package com.example.androidtesting.ui.add

import com.example.androidtesting.data.source.local.ShoppingItem

data class AddShoppingItemUiState(
    val shoppingItem: ShoppingItem? = null,
    val errorMessage: String? = null
)
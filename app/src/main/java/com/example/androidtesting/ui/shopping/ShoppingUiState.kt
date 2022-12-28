package com.example.androidtesting.ui.shopping

import com.example.androidtesting.data.source.local.ShoppingItem

data class ShoppingUiState(
    val shoppingItemList: List<ShoppingItem> = listOf(),
    val totalPrice: Int = 0,
    val loading: Boolean = false,
    val errorMessage: String? = null
)
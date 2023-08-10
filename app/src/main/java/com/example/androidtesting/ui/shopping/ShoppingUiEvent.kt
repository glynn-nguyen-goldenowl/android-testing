package com.example.androidtesting.ui.shopping

import com.example.androidtesting.data.source.local.ShoppingItem

sealed class ShoppingUiEvent {
    data class DeleteItem(val item: ShoppingItem): ShoppingUiEvent()
    data class InsertItem(val item: ShoppingItem): ShoppingUiEvent()
    data class ConsumeError(val error: String): ShoppingUiEvent()
    data class IncrementAmountItem(val item: ShoppingItem): ShoppingUiEvent()
    data class DecrementAmountItem(val item: ShoppingItem): ShoppingUiEvent()
}
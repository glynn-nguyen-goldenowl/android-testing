package com.example.androidtesting.ui.shopping

import com.example.androidtesting.data.source.local.ShoppingItem
import com.example.androidtesting.ui.picker.ImageListUiEvent

sealed class ShoppingUiEvent {
    data class DeleteItem(val item: ShoppingItem): ShoppingUiEvent()
    data class InsertItem(val item: ShoppingItem): ShoppingUiEvent()
    data class ConsumeError(val error: String): ShoppingUiEvent()
}
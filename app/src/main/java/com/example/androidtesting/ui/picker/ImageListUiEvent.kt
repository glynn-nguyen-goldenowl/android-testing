package com.example.androidtesting.ui.picker

sealed class ImageListUiEvent {
    data class Search(val searchIndex: String): ImageListUiEvent()
}
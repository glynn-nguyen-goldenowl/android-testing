package com.example.androidtesting.ui.picker

data class ImageListUiState(
    val imageUrls : List<String> = listOf(),
    val isLoading : Boolean = false,
    val errorMessage: String? = null
)
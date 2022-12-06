package com.example.androidtesting.data.source.remote.dto

data class ImageResponseDto(
    val hits: List<ImageResultDto>,
    val total: Int,
    val totalHits: Int
)
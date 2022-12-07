package com.example.androidtesting.data

sealed class DataResult<out R> {
    data class Success<out T>(val data: T): DataResult<T>()
    data class Error(val error: Throwable):  DataResult<Nothing>()
}
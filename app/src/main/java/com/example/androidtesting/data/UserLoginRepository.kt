package com.example.androidtesting.data

interface UserLoginRepository {
     fun validateUserName(userName: String): Boolean

    fun loggedIn(userName: String): Boolean
}
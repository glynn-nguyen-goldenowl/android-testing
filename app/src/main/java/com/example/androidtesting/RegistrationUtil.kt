package com.example.androidtesting

object RegistrationUtil {
    /**
     * the result is invalid if...
     * ... username is empty
     * ... password is less than two digits
     * ... confirmed password is not the same as password
     */
    fun validateRegistrationUserInput(
        username: String,
        password: String,
        confirmedPassword: String
    ): Boolean {
        return true
    }
}
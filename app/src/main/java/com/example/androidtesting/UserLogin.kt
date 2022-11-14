package com.example.androidtesting

class UserLogin {
    /**
      The current user logged in
   */
    private var currentUserLoggedIn = ""

    /**
      The userName is not valid if
      ...userName length is not in [5-8] letters
    */
    private fun validateUserName(userName: String): Boolean {
        if (userName.length < 5) {
            return false
        }
        if (userName.length > 8) {
            return false
        }
        return true
    }

    /**
      The result is valid if
      ... has no user logged before
      ... userName is valid
    */
    fun loggedIn(userName: String): Boolean {
        if (currentUserLoggedIn.isNotEmpty() && validateUserName(userName)) {
            currentUserLoggedIn = userName
            return true
        }
        return false
    }
}
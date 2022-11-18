package com.example.androidtesting.data

class UserLoginRepositoryImpl(
    private val userManager: UserManager
): UserLoginRepository {
    /**
    The userName is not valid if
    ...userName length is not in [5-8] letters
     */
    override fun validateUserName(userName: String): Boolean {
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
    override fun loggedIn(userName: String): Boolean {
        if (userManager.currentUserLoggedIn.isEmpty() && validateUserName(userName)) {
            userManager.currentUserLoggedIn = userName
            return true
        }
        return false
    }
}
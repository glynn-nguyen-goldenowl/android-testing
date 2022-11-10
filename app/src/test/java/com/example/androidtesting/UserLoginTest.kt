package com.example.androidtesting

import com.google.common.truth.Truth
import org.junit.Test

class UserLoginTest{
    @Test
    fun `5 letters userName returns true`(){
        val result = UserLogin().loggedIn("12345")
        Truth.assertThat(result).isFalse()
    }
    // HOMEWORK
    // valid userName return true
    // invalid userName return false
    // refactor UserLogin class.
    // apply setUp & tearDown
}
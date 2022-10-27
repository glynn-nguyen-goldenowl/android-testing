package com.example.androidtesting


import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest{

    @Test
    fun `empty_username_returns_false`(){
        val result = RegistrationUtil.validateRegistrationUserInput(
            username = "",
            password = "123",
            confirmedPassword = "123"
        )
        assertThat(result).isFalse()
    }

    // password less than two digits
    // confirmed password not the same as password.
    // valid user input
}
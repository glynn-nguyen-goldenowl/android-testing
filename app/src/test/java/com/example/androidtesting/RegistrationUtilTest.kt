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

    @Test
    fun `password less than two digits`(){
        val result = RegistrationUtil.validateRegistrationUserInput(
            username = "abcd",
            password = "1",
            confirmedPassword = "1"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `confirmed password not the same as password`(){
        val result = RegistrationUtil.validateRegistrationUserInput(
            username = "abcd",
            password = "12",
            confirmedPassword = "123"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `valid user input`(){
        val result = RegistrationUtil.validateRegistrationUserInput(
            username = "abcd",
            password = "123",
            confirmedPassword = "123"
        )
        assertThat(result).isTrue()
    }
}
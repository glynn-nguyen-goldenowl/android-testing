package com.example.androidtesting

import com.example.androidtesting.data.UserLoginRepository
import com.example.androidtesting.data.UserLoginRepositoryImpl
import com.example.androidtesting.data.UserManager
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserLoginTest{
    // refactor UserLogin class.

    private lateinit var userManager: UserManager
    private lateinit var userLoginRepository: UserLoginRepository

    // apply setUp & tearDown
    @Before
    fun setUp(){
        userManager = UserManager()
        userLoginRepository = UserLoginRepositoryImpl(userManager)
    }

    @After
    fun tearDown(){

    }

    // HOMEWORK
    // valid userName return true
    @Test
    fun `valid userName return true`(){
        val result = userLoginRepository.loggedIn("12345678")
        Truth.assertThat(result).isTrue()
    }
    // invalid userName return false
    @Test
    fun `invalid userName return false`(){
        val result = userLoginRepository.loggedIn("1234")
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `userName length 9 return false`(){
        val result = userLoginRepository.loggedIn("123456789")
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `user logged before`(){
        val resultFirst = userLoginRepository.loggedIn("12345")
        Truth.assertThat(resultFirst).isTrue()

        val resultSecond = userLoginRepository.loggedIn("1234578")
        Truth.assertThat(resultSecond).isFalse()
    }
}
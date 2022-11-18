package com.example.androidtesting

import com.google.common.truth.Truth
import org.junit.Test


/**
 * HOME WORK:
 *  unit test for W2Homework
 *  functions: fib and checkBraces are not correct. Fix that
 */
class W2HomeworkTest{
    @Test
    fun `fibonacci`(){
        val result = W2Homework().fib(20)
        Truth.assertThat(result).isEqualTo(6765)
    }

    @Test
    fun `fibonacci_0`(){
        val result = W2Homework().fib(0)
        Truth.assertThat(result).isEqualTo(0)
    }

    @Test
    fun `fibonacci_3`(){
        val result = W2Homework().fib(3)
        Truth.assertThat(result).isEqualTo(2)
    }

    @Test
    fun `checkBraces_correct`(){
        val result = W2Homework().checkBraces("(1(2(3)))")
        Truth.assertThat(result).isTrue()
    }

    @Test
    fun `checkBraces_less`(){
        val result = W2Homework().checkBraces("(12(3)))")
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `checkBraces_greater`(){
        val result = W2Homework().checkBraces("(1(2(3)")
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `checkBraces_example`(){
        val result = W2Homework().checkBraces("(a * b))")
        Truth.assertThat(result).isFalse()
    }
}

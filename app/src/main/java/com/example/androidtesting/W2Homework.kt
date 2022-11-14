package com.example.androidtesting

class W2Homework {
    /**
     * Returns the n-th fibonacci number
     * They are defined like this:
     * fib(0) = 0
     * fib(1) = 1
     * fib(n) = fib(n-2) + fib(n-1)
     */
    fun fib(n: Int): Long {
        if(n == 0 || n == 1) {
            return n.toLong()
        }
        var a = 0L
        var b = 1L
        var sum = 0L
        (2 ..  n).forEach { _ ->
            sum = a + b
            a = b
            b = sum
        }
        return sum
    }

    /**
     * Checks if the braces are set correctly
     * e.g. "(a * b))" should return false
     */
    fun checkBraces(string: String): Boolean {
        //Just check round brackets
        var stack = 0
        string.forEach {
            when(it){
                '(' -> stack++
                ')' -> {
                    if (stack < 1) return false
                    stack--
                }
            }
        }
        if (stack != 0) return false
        return true
    }
}
package com.example.androidtesting.util

import java.text.NumberFormat
import java.util.*

object CurrencyFormat {
    fun format(value: Int, currencyCode: String = "USD"): String{
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance(currencyCode)
        return format.format(value)
    }
}
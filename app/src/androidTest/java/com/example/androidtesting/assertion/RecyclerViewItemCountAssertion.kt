package com.example.androidtesting.assertion

import android.view.View
import androidx.recyclerview.widget.RecyclerView

import androidx.test.espresso.NoMatchingViewException

import androidx.test.espresso.ViewAssertion
import com.google.common.truth.Truth

class RecyclerViewItemCountAssertion(private val expectedCount: Int) :ViewAssertion {
    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        Truth.assertThat(adapter!!.itemCount).isEqualTo(expectedCount)
    }
}
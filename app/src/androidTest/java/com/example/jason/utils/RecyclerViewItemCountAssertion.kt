package com.example.jason.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import org.junit.Assert

class RecyclerViewItemCountAssertion constructor(private val expectedCount: Int) : ViewAssertion {

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView: RecyclerView = view as RecyclerView
        val adapter = recyclerView.adapter

        Assert.assertEquals(expectedCount, adapter?.itemCount)
    }
}
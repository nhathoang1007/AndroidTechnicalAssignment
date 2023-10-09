package com.example.jason

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


fun <T> LiveData<T>.getOrAwaitValue(): T? {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(2, TimeUnit.SECONDS)) {
        this.removeObserver(observer)
        return null
    }

    @Suppress("UNCHECKED_CAST")
    return data
}

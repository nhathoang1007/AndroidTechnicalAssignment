package com.example.jason.utils

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.not


inline fun <R> loopCallWithTimeout(timeout: Int = 30, call: () -> R): Result<R> {
    var countTime = 0
    while (countTime < timeout) {
        try {
            return Result.success(call())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        countTime ++
        Thread.sleep(1000)
    }
    return Result.failure(IllegalStateException("Call error"))
}

inline fun <R> safeCall(call: () -> R): Result<R> {
    return try {
        Result.success(call())
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }
}

fun Any.getMatcher(): Matcher<View> {
    return when (this) {
        is String -> withText(this)
        is Int -> withId(this)
        else -> throw IllegalArgumentException("Wrong type! Accept types should be String or Int")
    }
}

fun Any.getView(): ViewInteraction {
    return onView(getMatcher())
}

fun Int.getTextFromView(): String {
    val matcher = getView()
    var text = String()
    matcher.perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(TextView::class.java)
        }

        override fun getDescription(): String {
            return "Text of the view"
        }

        override fun perform(uiController: UiController, view: View) {
            val tv = view as TextView
            text = tv.text.toString()
        }
    })

    return text
}

fun Int.matchLabel(label: String) {
    getView().check(matches(withText(label)))
}

fun Any.performClick() {
    when (this) {
        is ViewInteraction -> {
            perform(click())
        }
        else -> {
            getView().perform(click())
        }
    }
    Thread.sleep(500L)
}

fun Int.performClickAfterAppeared() {
    getView().perform(click())
}

fun Int.inputText(value: String) {
    onView(
        Matchers.allOf(withId(this))
    ).perform(
        ViewActions.replaceText(value)
    )
    Thread.sleep(500L)
}

fun Int.closeKeyboard() {
    onView(
        Matchers.allOf(withId(this))
    ).perform(
        ViewActions.closeSoftKeyboard()
    )
    Thread.sleep(500L)
}
//
//fun Int.performClickOnFirstItem(text: String){
//    onView(
//        Utils.first(
//            Matchers.allOf(
//                withId(this),
//                withText(text),
//                isDisplayed()
//            )
//        )
//    ).perform(click())
//}
//
fun Int.performClickOnSelectedPosition(position: Int) {
    onView(
        Matchers.allOf(withId(this), isDisplayed())
    ).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
            position,
            click()
        )
    )
}

fun Any.isGone() {
    when (this) {
        is ViewInteraction -> {
            check(matches(not(isDisplayed())))
        }
        else -> {
            getView().check(matches(not(isDisplayed())))
        }
    }
}

fun Int.waitForAppeared() {
    onView(ViewMatchers.isRoot()).perform(WaitViewAction.waitViewAppeared(this))
}

fun Any.isDisplay() {
    Thread.sleep(500L)
    when (this) {
        is ViewInteraction -> {
            check(matches(isDisplayed()))
        }
        else -> {
            getView().check(matches(isDisplayed()))
        }
    }
}

fun Any.matchVisibility(visibility: ViewMatchers.Visibility){
    Thread.sleep(500L)
    when (this) {
        is ViewInteraction -> {
            check(matches(isDisplayed()))
        }

        else -> {
            getView().check(matches(withEffectiveVisibility(visibility)))
        }
    }

}

fun Any.matchedAlpha(alpha: Float) {
    matchViewStatus(value = alpha, status = ViewStatus.ALPHA)
}

fun Any.isEnabled(isEnabled: Boolean) {
    matchViewStatus(value = isEnabled, status = ViewStatus.ENABLED)
}

fun checkMatchedAlpha(alpha: Float): ViewAssertion {
    return matches(getAlphaMatcher(alpha, ViewStatus.ALPHA))
}

private fun Any.matchViewStatus(value: Any, status: ViewStatus) {
    when (this) {
        is ViewInteraction -> {
            check(matches(getAlphaMatcher(value, status)))
        }
        else -> {
            getView().check(matches(getAlphaMatcher(value, status)))
        }
    }
}

private fun getAlphaMatcher(value: Any, status: ViewStatus): Matcher<View> {
    return object : BoundedMatcher<View, View>(View::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("with alpha: ")
        }

        override fun matchesSafely(item: View?): Boolean {
            return when (status) {
                ViewStatus.ALPHA -> item?.alpha == value as Float
                ViewStatus.ENABLED -> item?.isEnabled == value as Boolean
            }
        }

    }
}

enum class ViewStatus {
    ALPHA,
    ENABLED,
}

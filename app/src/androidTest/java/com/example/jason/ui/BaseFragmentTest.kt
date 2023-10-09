package com.example.jason.ui

import androidx.annotation.StringRes
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.jason.R
import com.example.jason.TestActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseFragmentTest<T : BaseFragment<*, *>> {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(TestActivity::class.java)

    private lateinit var scenario: ActivityScenario<TestActivity>


    private lateinit var testFragment: T

    private lateinit var testActivity: TestActivity

    abstract fun initFragmentInstance(): T

    @Before
    open fun setup() {
        scenario = activityScenarioRule.scenario
        scenario.moveToState(Lifecycle.State.RESUMED)

        scenario.onActivity { activity ->
            testActivity = activity
            testFragment = initFragmentInstance().also { fragment ->
                activity.supportFragmentManager.commit {
                    replace(
                        R.id.test_frame_layout,
                        fragment
                    )
                    attach(fragment)
                    addToBackStack(fragment::class.simpleName)
                }
            }
        }
    }

    protected fun getString(@StringRes id: Int): String {
        return testActivity.getString(id)
    }

    protected fun getString(@StringRes id: Int, appendString: String): String {
        return testActivity.getString(id, appendString)
    }

    @After
    fun tearDown() {
        scenario.moveToState(Lifecycle.State.DESTROYED)
        scenario.close()
    }
}
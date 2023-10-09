package com.example.jason.ui.team

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.jason.R
import com.example.jason.ui.BaseFragmentTest
import com.example.jason.ui.teams.TeamsFragment
import com.example.jason.utils.matchLabel
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TeamFragmentTest : BaseFragmentTest<TeamsFragment>() {

    override fun initFragmentInstance() = TeamsFragment()

    @Test
    fun test() {
        TeamScreen.matchTitle(getString(R.string.page_title_teams))
        TeamScreen.waitForListAppeared()
        TeamScreen.matchTeamCount(10)
    }
}
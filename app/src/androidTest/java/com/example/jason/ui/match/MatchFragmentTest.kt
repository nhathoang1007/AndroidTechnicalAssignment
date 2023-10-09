package com.example.jason.ui.match

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.jason.R
import com.example.jason.ui.BaseFragmentTest
import com.example.jason.ui.matches.MatchesFragment
import com.example.jason.ui.team.TeamScreen
import com.example.jason.utils.isDisplay
import com.example.jason.utils.matchLabel
import com.example.jason.utils.performClick
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MatchFragmentTest : BaseFragmentTest<MatchesFragment>() {

    override fun initFragmentInstance() = MatchesFragment()

    @Test
    fun test_match_team_show_correctly() {
        MatchScreen.titleTv.matchLabel(getString(R.string.title_matches))
        MatchScreen.matchTabCount()

        MatchScreen.waitForListAppeared()
        MatchScreen.matchTeamCount(23)

        MatchScreen.performSelectPreviousTab()
        Thread.sleep(500L)
        MatchScreen.matchTeamCount(55)
    }

    @Test
    fun test_team_selection_dialog_shown_when_click_button_filter() {
        MatchScreen.waitForListAppeared()
        MatchScreen.performFilter()
        MatchScreen.selectedTeamTv.isDisplay()
    }
}
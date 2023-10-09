package com.example.jason.ui.match

import androidx.annotation.IdRes
import com.example.jason.R
import com.example.jason.utils.RecyclerViewItemCountAssertion
import com.example.jason.utils.getView
import com.example.jason.utils.matchLabel
import com.example.jason.utils.performClick
import com.example.jason.utils.performClickOnSelectedPosition
import com.example.jason.utils.waitForAppeared

object MatchScreen {

    @IdRes
    const val titleTv: Int = R.id.tv_title

    @IdRes
    const val selectedTeamTv: Int = R.id.tv_select_team_title

    @IdRes
    private const val matchTimeTv: Int = R.id.tv_match_time

    @IdRes
    private const val matchRv: Int = R.id.rv_matches

    @IdRes
    private const val matchTabRv: Int = R.id.rv_match_types

    @IdRes
    private const val filterBtn: Int = R.id.btn_filter


    fun performSelectPreviousTab() {
        matchTabRv.performClickOnSelectedPosition(0)
    }

    fun performFilter() {
        filterBtn.performClick()
    }

    fun matchTabCount() {
        matchTabRv.getView().check(RecyclerViewItemCountAssertion(2))
    }

    fun waitForListAppeared() {
        matchTimeTv.waitForAppeared()
    }

    fun matchTeamCount(count: Int) {
        matchRv.getView().check(RecyclerViewItemCountAssertion(expectedCount = count))
    }
}
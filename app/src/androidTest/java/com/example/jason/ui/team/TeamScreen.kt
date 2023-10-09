package com.example.jason.ui.team

import androidx.annotation.IdRes
import com.example.jason.R
import com.example.jason.utils.RecyclerViewItemCountAssertion
import com.example.jason.utils.getView
import com.example.jason.utils.matchLabel
import com.example.jason.utils.waitForAppeared

object TeamScreen {

    @IdRes
    private const val titleTv: Int = R.id.tv_title

    @IdRes
    private const val teamNameTv: Int = R.id.tv_team_name

    @IdRes
    private const val teamRv: Int = R.id.rv_teams

    fun waitForListAppeared() {
        teamNameTv.waitForAppeared()
    }

    fun matchTitle(title: String) {
        titleTv.matchLabel(title)
    }

    fun matchTeamCount(count: Int) {
        teamRv.getView().check(
            RecyclerViewItemCountAssertion(
                expectedCount = count
            )
        )
    }
}
package com.example.jason.ui.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.data.store.MyPref
import com.example.domain.model.Match
import com.example.domain.usecase.FindMatchesUseCase
import com.example.domain.usecase.FlowResult
import com.example.domain.usecase.GetMatchesUseCase
import com.example.domain.usecase.GetTeamsUseCase
import com.example.jason.ui.BaseViewModel
import com.example.jason.ui.adapter.match.MatchDateHeader
import com.example.jason.ui.adapter.match.MatchModel
import com.example.jason.ui.adapter.match.MatchRow
import com.example.jason.ui.adapter.tab.TabModel
import com.example.jason.ui.adapter.team.TeamModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val findMatchesUseCase: FindMatchesUseCase,
    private val getTeamsUseCase: GetTeamsUseCase,
    private val pref: MyPref,
) : BaseViewModel() {

    private val _tabs = MutableLiveData<List<TabModel>>().apply {
        postValue(listOf(TabModel("Previous"), TabModel("Upcoming", true)))
    }
    val tabs: LiveData<List<TabModel>> = _tabs

    private val _matches = MutableLiveData<List<MatchRow>>()
    val matches: LiveData<List<MatchRow>> = _matches
    private var matchesCache = listOf<Match>()

    private val _teams = MutableLiveData<List<TeamModel>>()
    val teams: LiveData<List<TeamModel>> = _teams

    private val _matchHighlight = MutableLiveData<MatchModel>()
    val matchHighlight: LiveData<MatchModel> = _matchHighlight

    fun getTeams() {
        viewModelScope.launch {
            getTeamsUseCase.execute()
                .onStart { _isLoading.postValue(true) }
                .collect { result ->
                    when (result) {
                        is FlowResult.Success -> {
                            _teams.postValue(result.data.map { TeamModel(it) })
                        }
                        is FlowResult.Error -> {
                            _isLoading.postValue(false)
                            handleError()
                        }
                    }
                }
            getMatches()
        }
    }

    private fun findMatches(teamId: String? = null) {
        viewModelScope.launch {
            getMatches(teamId)
        }
    }

    private suspend fun getMatches(teamId: String? = null) {
        if (teamId.isNullOrEmpty()) {
            getMatchesUseCase.execute()
        } else {
            findMatchesUseCase.execute(teamId)
        }.onStart { _isLoading.postValue(true) }
            .onCompletion { _isLoading.postValue(false) }
            .collect { result ->
                when (result) {
                    is FlowResult.Success -> onGetMatchesSuccess(result.data)
                    is FlowResult.Error -> {
                        handleError()
                    }
                }
            }
    }

    private fun onGetMatchesSuccess(result: List<Match>) {
        matchesCache = result
        populateMatches()
    }

    private fun populateMatches() {
        val tabs = _tabs.value ?: listOf()
        val reminders = pref.getReminders()

        val matches = matchesCache.filter { match ->
            val isUpcomingSelected = tabs.find { it.title == "Upcoming" }?.isSelected == true
            match.isUpcoming == isUpcomingSelected
        }.map {
            MatchModel(it, it.home.getTeam(), it.away.getTeam()).apply {
                isScheduled = reminders.contains(unique)
            }
        }

        matches.sortedBy { it.calendar.timeInMillis }

        val rows = mutableListOf<MatchRow>()
        matches.groupBy { it.displayDate }.forEach { (t, u) ->
            rows.add(MatchDateHeader(t))
            rows.addAll(u.map { it.copy() })
        }

        _matches.postValue(rows)
    }

    private fun String.getTeam(): TeamModel? {
        val teams = _teams.value ?: listOf()

        return teams.find { it.name == this }
    }

    fun handleSelectedTab(select: TabModel) {
        val tabs = _tabs.value ?: listOf()
        tabs.onEach {
            it.isSelected = it.title == select.title
        }.map { it.copy() }.let {
            _tabs.postValue(it)
            populateMatches()
        }
    }

    fun handleSelectedTeam(select: TeamModel) {
        val teams = _teams.value ?: listOf()
        teams.onEach {
            it.isSelected = if (it.id == select.id) {
                !it.isSelected
            } else {
                false
            }
        }.map { it.copy() }.let {
            _teams.postValue(it)
            findMatches(if (!select.isSelected) select.id else null)
        }
    }

    fun handleMatchHighlightSelected(selected: MatchModel) {
        _matchHighlight.postValue(selected)
    }

    fun handleReminderStored() {
        populateMatches()
    }
}
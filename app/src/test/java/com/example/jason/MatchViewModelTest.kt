package com.example.jason

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.data.store.MyPref
import com.example.domain.model.Match
import com.example.domain.model.Team
import com.example.domain.usecase.FindMatchesUseCase
import com.example.domain.usecase.FlowResult
import com.example.domain.usecase.GetMatchesUseCase
import com.example.domain.usecase.GetTeamsUseCase
import com.example.jason.ui.adapter.match.MatchModel
import com.example.jason.ui.adapter.tab.TabModel
import com.example.jason.ui.adapter.team.TeamModel
import com.example.jason.ui.matches.MatchesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MatchViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var getMatchesUseCase: GetMatchesUseCase

    @Mock
    lateinit var findMatchesUseCase: FindMatchesUseCase

    @Mock
    lateinit var getTeamsUseCase: GetTeamsUseCase

    @Mock
    lateinit var pref: MyPref

    private lateinit var viewModel: MatchesViewModel

    @Before
    fun setup() {
        viewModel = MatchesViewModel(getMatchesUseCase, findMatchesUseCase, getTeamsUseCase, pref)
    }

    @Test
    fun `verify getTeams return error when exception correctly`() {
        // WHEN
        Mockito.`when`(getTeamsUseCase.execute()).thenReturn(flow {
            emit(FlowResult.Error(IllegalStateException("Any")))
        })
        viewModel.getTeams()
        val teams = viewModel.teams.getOrAwaitValue()
        val error = viewModel.isError.getOrAwaitValue()

        // THEN
        Assert.assertTrue(teams.isNullOrEmpty())
        Assert.assertTrue(error == true)

    }


    @Test
    fun `verify getTeams return teams without error correctly`() {
        // GIVEN
        val mockTeam = mockTeams()

        // WHEN
        viewModel.getTeams()
        val teams = viewModel.teams.getOrAwaitValue()
        val error = viewModel.isError.getOrAwaitValue()

        // THEN
        Assert.assertTrue(teams.isNullOrEmpty().not())
        Assert.assertTrue(teams?.first()?.id == mockTeam.id)
        Assert.assertTrue(error != true)
    }

    @Test
    fun `verify getMatches return upcoming correctly`() {
        // GIVEN
        mockTeams()
        val previousMatch = 1
        val upcomingMatch = 2
        mockMatches(previousMatch, upcomingMatch)

        // WHEN
        viewModel.handleSelectedTab(TabModel(title = "Upcoming"))
        viewModel.getTeams()
        val matches = viewModel.matches.getOrAwaitValue()

        // THEN
        Assert.assertEquals(matches?.filterIsInstance(MatchModel::class.java)?.size, upcomingMatch)
    }

    @Test
    fun `verify getMatches return previous correctly`() {
        // GIVEN
        mockTeams()
        val previousMatch = 3
        val upcomingMatch = 1
        mockMatches(previousMatch, upcomingMatch)

        // WHEN
        viewModel.handleSelectedTab(TabModel(title = "Previous"))
        viewModel.getTeams()
        val matches = viewModel.matches.getOrAwaitValue()

        // THEN
        Assert.assertEquals(matches?.filterIsInstance(MatchModel::class.java)?.size, previousMatch)
    }

    @Test
    fun `verify findMatch return correctly`() {
        // GIVEN
        val mockTeam = mockTeams()
        val previousMatch = 5
        val upcomingMatch = 4
        mockMatches(previousMatch, upcomingMatch)

        // WHEN
        viewModel.getTeams()
        viewModel.handleSelectedTab(TabModel(title = "Previous"))
        viewModel.handleSelectedTeam(TeamModel(mockTeam))
        val matches = viewModel.matches.getOrAwaitValue()
        // THEN
        Assert.assertEquals(matches?.filterIsInstance(MatchModel::class.java)?.size, previousMatch)
    }

    @Test
    fun `verify handleMatchHighlightSelected correctly`() {
        // GIVE
        val mockHighlight = "Any"
        val mockMatch = Mockito.mock(MatchModel::class.java).apply {
            Mockito.`when`(highlight).thenReturn(mockHighlight)
        }

        // WHEN
        viewModel.handleMatchHighlightSelected(mockMatch)
        val match = viewModel.matchHighlight.getOrAwaitValue()

        // THEN
        Assert.assertEquals(match?.highlight, mockHighlight)

    }

    private fun mockTeams(): Team {
        // GIVEN
        val mockTeam = Team("id", "Team Cool Eagles", logo = "")

        // WHEN
        Mockito.`when`(getTeamsUseCase.execute()).thenReturn(flow {
            emit(FlowResult.Success(listOf(mockTeam)))
        })
        return mockTeam
    }

    private fun mockMatches(previous: Int, upcoming: Int, teamId: String? = null) {
        // GIVEN
        val mockMatch = Match(
            date = "2022-04-23T18:00:00.000Z",
            description = "Team Cool Eagles vs. Team Red Dragons",
            home = "Team Cool Eagles",
            away = "Team Red Dragons",
            winner = null,
            highlights = null,
            isUpcoming = false
        )
        val mockMatches = mutableListOf<Match>()
        repeat(previous) {
            mockMatches.add(mockMatch.copy(winner = mockMatch.home))
        }
        repeat(upcoming) {
            mockMatches.add(mockMatch.copy(isUpcoming = true))
        }

        // WHEN
        Mockito.`when`(getMatchesUseCase.execute()).thenReturn(
            flow {
                emit(FlowResult.Success(mockMatches))
            }
        )

        if (teamId.isNullOrEmpty().not()) {
            Mockito.`when`(findMatchesUseCase.execute(teamId!!)).thenReturn(
                flow {
                    emit(FlowResult.Success(mockMatches))
                }
            )
        }
    }
}
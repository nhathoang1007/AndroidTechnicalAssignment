package com.example.domain.repository

import com.example.domain.usecase.FlowResult
import com.example.domain.model.Match
import com.example.domain.model.Team
import kotlinx.coroutines.flow.Flow

interface CoreRepository {

    fun getTeams(): Flow<FlowResult<List<Team>>>
    fun getMatches(): Flow<FlowResult<List<Match>>>
    fun findTeamMatches(teamId: String): Flow<FlowResult<List<Match>>>
}
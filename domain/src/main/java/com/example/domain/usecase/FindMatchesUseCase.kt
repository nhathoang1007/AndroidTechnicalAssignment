package com.example.domain.usecase

import com.example.domain.model.Match
import com.example.domain.repository.CoreRepository
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class FindMatchesUseCase @Inject constructor(
    private val repository: CoreRepository
) {

    @ExperimentalCoroutinesApi
    private operator fun invoke(teamId: String): Flow<FlowResult<List<Match>>> {
        return execute(teamId)
    }

    fun execute(teamId: String): Flow<FlowResult<List<Match>>> {
        return repository.findTeamMatches(teamId).execute()
    }
}
package com.example.domain.usecase

import com.example.domain.model.Match
import com.example.domain.repository.CoreRepository
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class GetMatchesUseCase @Inject constructor(
    private val repository: CoreRepository
) {

    @ExperimentalCoroutinesApi
    private operator fun invoke(): Flow<FlowResult<List<Match>>> {
        return execute()
    }

    fun execute(): Flow<FlowResult<List<Match>>> {
        return repository.getMatches().execute()
    }
}
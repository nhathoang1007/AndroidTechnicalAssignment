package com.example.domain.usecase

import com.example.domain.model.Team
import com.example.domain.repository.CoreRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

class GetTeamsUseCase @Inject constructor(
    private val repository: CoreRepository
) {

    @ExperimentalCoroutinesApi
    private operator fun invoke(): Flow<FlowResult<List<Team>>> {
        return execute()
    }

    fun execute(): Flow<FlowResult<List<Team>>> {
        return repository.getTeams().execute()
    }
}
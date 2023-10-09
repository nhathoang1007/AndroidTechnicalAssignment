package com.example.data.repository

import com.example.data.entity.MatchEntity
import com.example.data.entity.mapper.MatchesResponseMapper
import com.example.data.entity.mapper.TeamResponseMapper
import com.example.data.service.CoreApi
import com.example.domain.usecase.FlowResult
import com.example.domain.model.Match
import com.example.domain.model.Team
import com.example.domain.repository.CoreRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit

class CoreRepositoryImpl @Inject constructor(
    retrofit: Retrofit,
    private val teamMapper: TeamResponseMapper,
    private val matchMapper: MatchesResponseMapper,
): CoreRepository {

    private var service: CoreApi = retrofit.create(CoreApi::class.java)

    override fun getTeams(): Flow<FlowResult<List<Team>>> {
        return flow {
            emit(safeApiCall { service.getTeams() })
        }.map { result ->
            when (result) {
                is FlowResult.Success -> FlowResult.Success(teamMapper.mapFromEntity(result.data))
                is FlowResult.Error -> FlowResult.Error(result.exception)
                else -> FlowResult.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }

    override fun getMatches(): Flow<FlowResult<List<Match>>> {
        return flow {
            emit(safeApiCall { service.getMatches() })
        }.map { result ->
            when (result) {
                is FlowResult.Success -> FlowResult.Success(matchMapper.mapFromEntity(result.data))
                is FlowResult.Error -> FlowResult.Error(result.exception)
                else -> FlowResult.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }

    override fun findTeamMatches(teamId: String): Flow<FlowResult<List<Match>>> {
        return flow {
            emit(safeApiCall { service.findTeamMatches(teamId) })
        }.map { result ->
            when (result) {
                is FlowResult.Success -> FlowResult.Success(matchMapper.mapFromEntity(result.data))
                is FlowResult.Error -> FlowResult.Error(result.exception)
                else -> FlowResult.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }
}
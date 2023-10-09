package com.example.data.entity.mapper

import com.example.data.entity.response.TeamResponseEntity
import com.example.domain.model.Team
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeamResponseMapper @Inject constructor() {

    fun mapFromEntity(response: TeamResponseEntity): List<Team> {
        return response.teams.map { Team(it.id, it.name, it.logo) }
    }
}
package com.example.data.service

import com.example.data.entity.response.MatchResponseEntity
import com.example.data.entity.response.TeamResponseEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface CoreApi {

    @GET("teams")
    suspend fun getTeams(): TeamResponseEntity

    @GET("teams/matches")
    suspend fun getMatches(): MatchResponseEntity

    @GET("teams/{id}/matches")
    suspend fun findTeamMatches(@Path("id") teamId: String): MatchResponseEntity
}
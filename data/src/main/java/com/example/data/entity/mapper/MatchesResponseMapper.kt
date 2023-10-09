package com.example.data.entity.mapper

import com.example.data.entity.MatchEntity
import com.example.data.entity.response.MatchResponseEntity
import com.example.domain.model.Match
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchesResponseMapper @Inject constructor() {

    fun mapFromEntity(response: MatchResponseEntity): List<Match> {
        val matches = mutableListOf<Match>()
        matches.addAll(response.matches.previous.map { it.mapToMatch(false) })
        matches.addAll(response.matches.upcoming.map { it.mapToMatch(true) })

        return matches
    }

    private fun MatchEntity.mapToMatch(isUpcoming: Boolean): Match {
        return Match(date, description, home, away, winner, highlights, isUpcoming)
    }
}
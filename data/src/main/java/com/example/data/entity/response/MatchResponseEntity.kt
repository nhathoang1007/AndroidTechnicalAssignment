package com.example.data.entity.response

import com.example.data.entity.MatchEntity

data class MatchResponseEntity constructor(
    val matches: MatchTypeEntity
)

data class MatchTypeEntity constructor(
    val previous: List<MatchEntity>,
    val upcoming: List<MatchEntity>,
)
package com.example.domain.model

data class Match constructor(
    val date: String,
    val description: String,
    val home: String,
    val away: String,
    val winner: String?,
    val highlights: String?,
    val isUpcoming: Boolean,
)
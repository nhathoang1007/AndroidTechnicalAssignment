package com.example.data.entity

data class MatchEntity constructor(
    val date: String,
    val description: String,
    val home: String,
    val away: String,
    val winner: String?,
    val highlights: String?,
)
package com.example.data.store

interface MyPref {
    fun saveReminder(matchId: String)
    fun getReminders(): List<String>
}
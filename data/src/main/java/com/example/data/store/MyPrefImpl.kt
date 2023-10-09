package com.example.data.store

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import retrofit2.converter.moshi.MoshiConverterFactory

class MyPrefImpl @Inject constructor(
    @ApplicationContext context: Context
): MyPref {

    private val sharedPref: SharedPreferences
    private val moshi: Moshi by lazy {
        Moshi.Builder().build()
    }

    init {
        sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun saveReminder(matchId: String) {
        val reminders = getReminders().toMutableList()

        if (reminders.contains(matchId).not()) {
            reminders.add(matchId)
        }

        with (sharedPref.edit()) {
            val jsonAdapter = moshi.adapter(Array<String>::class.java)
            putString(MATCH_REMINDER_KEY, jsonAdapter.toJson(reminders.toTypedArray()))
            apply()
        }
    }

    override fun getReminders(): List<String> {
        val reminders = sharedPref.getString(MATCH_REMINDER_KEY, "")
        if (reminders.isNullOrEmpty()) return emptyList()

        val jsonAdapter= moshi.adapter(Array<String>::class.java)
        return jsonAdapter.fromJson(reminders)?.toList() ?: emptyList()
    }

    companion object {

        private const val PREFS_NAME = "ATA_Store"
        private const val MATCH_REMINDER_KEY = "MATCH_REMINDER"
    }
}
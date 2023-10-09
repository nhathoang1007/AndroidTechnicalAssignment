package com.example.jason.schedule

import android.content.Intent

internal const val MATCH_TIME_KEY = "MATCH_TIME_KEY"
internal const val MATCH_DESC_KEY = "MATCH_DESC_KEY"

fun Intent?.getMatchExtra(): Pair<String, String>? {
    val bundle = this?.extras ?: return null

    return bundle.run {
        Pair(
            getString(MATCH_TIME_KEY, ""),
            getString(MATCH_DESC_KEY, "")
        )
    }
}

fun Intent.putMatchExtra(data: Pair<String, String>) {
    putExtra(MATCH_TIME_KEY, data.first)
    putExtra(MATCH_DESC_KEY, data.second)
}
package com.example.jason.ui.adapter.match

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.Match
import com.example.jason.ui.adapter.team.TeamModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

sealed class MatchRow

data class MatchDateHeader constructor(
    val date: String
): MatchRow()

data class MatchModel constructor(
    val date: String,
    val description: String,
    val homeTeam: TeamModel?,
    val awayTeam: TeamModel?,
    val highlight: String? = null,
    var isScheduled: Boolean = false,
    var isUpcoming: Boolean = true,
): MatchRow() {


    val calendar: Calendar
        get() {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            val calendar = Calendar.getInstance().apply {
                time = format.parse(date) ?: Date()
            }
            return calendar
        }

    val time: String
        get() {
            val timeFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
            return timeFormat.format(calendar.time)
        }

    val displayDate: String
        get() {
            val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH)
            return dateFormat.format(calendar.time)
        }

    val unique: String
        get() {
            val homeTeamId = homeTeam?.id ?: UUID.randomUUID()
            val awayTeamId = awayTeam?.id ?: UUID.randomUUID()
            return date.plus(homeTeamId).plus(awayTeamId)
        }

    constructor(domain: Match, homeTeam: TeamModel?, awayTeam: TeamModel?) : this(
        date = domain.date,
        description = domain.description,
        homeTeam = homeTeam?.copy(
            isHomeTeam = true,
            isWinner = domain.winner == homeTeam.name
        ),
        awayTeam = awayTeam?.copy(
            isHomeTeam = false,
            isWinner = domain.winner == awayTeam.name
        ),
        highlight = domain.highlights,
        isUpcoming = domain.isUpcoming,
    )
}

object MatchDiffUtil : DiffUtil.ItemCallback<MatchRow>() {
    override fun areItemsTheSame(oldItem: MatchRow, newItem: MatchRow): Boolean {
        return if (oldItem is MatchModel && newItem is MatchModel) {
            oldItem.unique == newItem.unique
        } else if (oldItem is MatchDateHeader && newItem is MatchDateHeader) {
            oldItem.date == newItem.date
        } else false
    }

    override fun areContentsTheSame(oldItem: MatchRow, newItem: MatchRow): Boolean {
        return if (oldItem is MatchModel && newItem is MatchModel) {
            oldItem == newItem
        } else false
    }

}
package com.example.jason.ui.componets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.jason.databinding.ViewMatchTeamBinding
import com.example.jason.ui.adapter.team.TeamModel

class MatchTeamView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: ViewMatchTeamBinding

    var isUpcomingMatch: Boolean = true
    set(value) {
        field = value

        binding.tvScore.isVisible = isUpcomingMatch.not()
    }
    var team: TeamModel? = null
        set(value) {
            field = value

            value?.let { updateTeam(it) }
        }

    init {
        binding = ViewMatchTeamBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun updateTeam(team: TeamModel) {
        binding.tvTeamName.text = team.shortName
        Glide.with(context).load(team.logo).into(binding.imgTeamLogo)
        binding.tvScore.text = if (team.isWinner) {
            "2"
        } else {
            "1"
        }

        binding.viewGroup.layoutDirection = if (team.isHomeTeam) {
            View.LAYOUT_DIRECTION_LTR
        } else {
            View.LAYOUT_DIRECTION_RTL
        }
    }
}
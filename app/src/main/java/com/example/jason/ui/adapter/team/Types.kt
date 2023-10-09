package com.example.jason.ui.adapter.team

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.Team


data class TeamModel constructor(
    val id: String,
    val name: String,
    val logo: String,
    var isSelected: Boolean = false,
    val isHomeTeam: Boolean = true,
    val isWinner: Boolean = false,
) {

    val shortName: String
    get() {
        val split = name.split(" ")
        return split.map { it.first() }.joinToString(separator = "")
    }

    constructor(domain: Team) : this(
        id = domain.id,
        name = domain.name,
        logo = domain.logo,
        isSelected = false,
        isHomeTeam = false,
        isWinner = false,
    )
}

object TeamDiffUtil: DiffUtil.ItemCallback<TeamModel>() {
    override fun areItemsTheSame(oldItem: TeamModel, newItem: TeamModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TeamModel, newItem: TeamModel): Boolean {
        return oldItem == newItem
    }

}
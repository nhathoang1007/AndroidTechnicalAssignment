package com.example.jason.ui.adapter.team

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jason.databinding.ViewTeamBinding

class TeamAdapter(
    private val onSelectedTeam: ((TeamModel) -> Unit)? = null
) : ListAdapter<TeamModel, TeamAdapter.TeamViewHolder>(TeamDiffUtil) {

    inner class TeamViewHolder(private val binding: ViewTeamBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(position: Int) {
                val model = currentList[position]
                with(binding) {
                    tvTeamName.text = model.name.plus(" (${model.shortName})")
                    radio.isSelected = model.isSelected
                    radio.isVisible = onSelectedTeam != null
                    Glide.with(root.context).load(model.logo).into(imgTeamLogo)

                    root.setOnClickListener {
                        onSelectedTeam?.invoke(model)
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        return TeamViewHolder(
            ViewTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bind(position)
    }
}
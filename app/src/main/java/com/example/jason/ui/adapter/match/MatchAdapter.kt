package com.example.jason.ui.adapter.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jason.databinding.ViewMatchBinding
import com.example.jason.databinding.ViewMatchHeaderBinding

class MatchAdapter(
    private val onWatchHighlight: (MatchModel) -> Unit,
    private val onStartSchedule: (MatchModel) -> Unit,
) : ListAdapter<MatchRow, RecyclerView.ViewHolder>(MatchDiffUtil) {

    companion object {

        private const val HEADER = 0
        private const val MATCH = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is MatchDateHeader -> HEADER
            else -> MATCH
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> HeaderViewHolder(
                ViewMatchHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> MatchViewHolder(
                ViewMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MatchViewHolder -> holder.bind(position)
            is HeaderViewHolder -> holder.bind(position)
        }
    }

    inner class MatchViewHolder(private val binding: ViewMatchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val model = currentList[position] as MatchModel
            with(binding) {
                tvMatchTime.text = model.time
                homeTeam.team = model.homeTeam
                homeTeam.isUpcomingMatch = model.isUpcoming
                awayTeam.team = model.awayTeam
                awayTeam.isUpcomingMatch = model.isUpcoming

                btnWatchHighlight.isVisible = model.highlight.isNullOrEmpty().not()
                btnWatchHighlight.setOnClickListener {
                    onWatchHighlight.invoke(model)
                }

                imgSchedule.isVisible = model.isUpcoming
                imgSchedule.isSelected = model.isScheduled
                imgSchedule.setOnClickListener {
                    onStartSchedule.invoke(model)
                }
            }
        }
    }

    inner class HeaderViewHolder(private val binding: ViewMatchHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val model = currentList[position] as MatchDateHeader
            with(binding) {
                tvMatchHeader.text = model.date
            }
        }
    }

}
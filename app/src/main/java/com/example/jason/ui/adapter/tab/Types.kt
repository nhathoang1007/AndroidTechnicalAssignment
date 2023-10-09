package com.example.jason.ui.adapter.tab

import androidx.recyclerview.widget.DiffUtil

data class TabModel constructor(
    val title: String,
    var isSelected: Boolean = false
)

object TabDiffUtil: DiffUtil.ItemCallback<TabModel>() {
    override fun areItemsTheSame(oldItem: TabModel, newItem: TabModel): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: TabModel, newItem: TabModel): Boolean {
        return oldItem == newItem
    }
}
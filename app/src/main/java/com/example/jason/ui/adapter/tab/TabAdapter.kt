package com.example.jason.ui.adapter.tab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jason.databinding.ViewTabBinding

class TabAdapter(
    private val onSelectedTab: (TabModel) -> Unit
) : ListAdapter<TabModel, TabAdapter.TabViewHolder>(TabDiffUtil) {

    inner class TabViewHolder(private val binding: ViewTabBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(position: Int) {
                val model = currentList[position]
                with(binding) {
                    tvTabTitle.text = model.title
                    tvTabTitle.isSelected = model.isSelected

                    root.setOnClickListener {
                        if (model.isSelected) return@setOnClickListener

                        onSelectedTab.invoke(model)
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        return TabViewHolder(
            ViewTabBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        holder.bind(position)
    }
}
package com.example.jason.ui.teams

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jason.databinding.FragmentHomeBinding
import com.example.jason.ui.BaseFragment
import com.example.jason.ui.adapter.team.TeamAdapter
import com.example.jason.ui.adapter.team.TeamModel
import com.example.jason.ui.matches.MatchesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamsFragment : BaseFragment<MatchesViewModel, FragmentHomeBinding>() {
    override val isViewModelActivityProvider: Boolean
        get() = true
    override val vmClass: Class<MatchesViewModel>
        get() = MatchesViewModel::class.java
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    private val teamAdapter: TeamAdapter by lazy {
        TeamAdapter()
    }

    override fun initView() {
        binding.rvTeams.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = teamAdapter
        }
    }

    override fun subscribeEvents() {
        viewModel.teams.observe(viewLifecycleOwner, this::populateTeams)
        viewModel.isLoading.observe(viewLifecycleOwner, this::showLoading)
    }

    private fun populateTeams(teams: List<TeamModel>) {
        teamAdapter.submitList(teams.map { it.copy() })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }
}
package com.example.jason.ui.matches

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jason.databinding.FragmentTeamSelectionBinding
import com.example.jason.ui.adapter.team.TeamAdapter
import com.example.jason.ui.adapter.team.TeamModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamSelectionBottomSheetDialog: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTeamSelectionBinding

    private val viewModel: MatchesViewModel by lazy {
        ViewModelProvider(requireActivity())[MatchesViewModel::class.java]
    }

    private val teamAdapter: TeamAdapter by lazy {
        TeamAdapter {
            viewModel.handleSelectedTeam(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamSelectionBinding.inflate(inflater, container, false)

        initView()
        subscribeEvents()
        return binding.root
    }

    private fun initView() {
        binding.rvTeams.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = teamAdapter
        }
    }

    private fun subscribeEvents() {
        viewModel.teams.observe(viewLifecycleOwner, this::populateTeams)
    }

    private fun populateTeams(teams: List<TeamModel>) {
        teamAdapter.submitList(teams.map { it.copy() })
    }

    companion object {

        fun newInstance() = TeamSelectionBottomSheetDialog()
    }
}
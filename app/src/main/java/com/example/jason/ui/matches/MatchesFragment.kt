package com.example.jason.ui.matches

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jason.databinding.FragmentDashboardBinding
import com.example.jason.schedule.ScheduleManager
import com.example.jason.ui.BaseFragment
import com.example.jason.ui.adapter.match.MatchAdapter
import com.example.jason.ui.adapter.match.MatchModel
import com.example.jason.ui.adapter.match.MatchRow
import com.example.jason.ui.adapter.tab.TabAdapter
import com.example.jason.ui.adapter.tab.TabModel
import com.example.jason.ui.adapter.team.TeamModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MatchesFragment : BaseFragment<MatchesViewModel, FragmentDashboardBinding>() {
    override val isViewModelActivityProvider: Boolean
        get() = true
    override val vmClass: Class<MatchesViewModel>
        get() = MatchesViewModel::class.java
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDashboardBinding
        get() = FragmentDashboardBinding::inflate

    @Inject
    lateinit var scheduleManager: ScheduleManager

    private val tabAdapter: TabAdapter by lazy {
        TabAdapter { select ->
            viewModel.handleSelectedTab(select)
        }
    }

    private var pendingScheduleMatch: MatchModel? = null

    private val matchAdapter: MatchAdapter by lazy {
        MatchAdapter(
            onWatchHighlight = {
                viewModel.handleMatchHighlightSelected(it)
                highlightDialog.show(childFragmentManager, null)
            },
            onStartSchedule = {
                pendingScheduleMatch = it
                if (shouldRequestNotificationPermission().not()) {
                    startScheduleMatch()
                }
            }
        )
    }

    private val requestPermissionNotification = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startScheduleMatch()
        } else {
            Toast.makeText(
                requireContext(),
                "We cannot notify to you when notification permission denied!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private val teamFilterDialog: TeamSelectionBottomSheetDialog by lazy {
        TeamSelectionBottomSheetDialog.newInstance()
    }

    private val highlightDialog: HighlightBottomSheetDialog by lazy {
        HighlightBottomSheetDialog.newInstance()
    }

    override fun initView() {
        super.initView()

        intMatchTabs()
        intMatches()
    }

    override fun subscribeEvents() {
        super.subscribeEvents()

        binding.btnFilter.setOnClickListener {
            teamFilterDialog.show(childFragmentManager, null)
        }

        viewModel.tabs.observe(viewLifecycleOwner, this::populateTabs)
        viewModel.matches.observe(viewLifecycleOwner, this::populateMatches)
        viewModel.teams.observe(viewLifecycleOwner, this::populateTeamsFiltered)
        viewModel.isLoading.observe(viewLifecycleOwner, this::showLoading)
    }

    private fun intMatchTabs() {
        binding.rvMatchTypes.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = tabAdapter
        }
    }

    private fun populateTabs(tabs: List<TabModel>) {
        tabAdapter.submitList(tabs.map { it.copy() })
    }

    private fun intMatches() {
        binding.rvMatches.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = matchAdapter
        }
    }

    private fun populateMatches(matches: List<MatchRow>) {
        matchAdapter.submitList(matches) {
            binding.rvMatches.smoothScrollToPosition(0)
        }
    }

    private fun populateTeamsFiltered(matches: List<TeamModel>) {
        binding.iconFiltered.isVisible = matches.any { it.isSelected }

        if (teamFilterDialog.isVisible) {
            binding.root.postDelayed({
                teamFilterDialog.dismiss()
            }, 1000L)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun startScheduleMatch() {
        if (pendingScheduleMatch == null) return

        scheduleManager.startSchedule(requireActivity(), pendingScheduleMatch!!) {
            viewModel.handleReminderStored()
            pendingScheduleMatch = null
        }
    }

    private fun shouldRequestNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val check = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            )
            val isGranted = check == PackageManager.PERMISSION_GRANTED
            if (isGranted.not()) {
                requestPermissionNotification.launch(Manifest.permission.POST_NOTIFICATIONS)
            }

            isGranted.not()
        } else {
            false
        }
    }
}
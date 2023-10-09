package com.example.jason.ui.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.example.jason.databinding.FragmentHighlightBinding
import com.example.jason.ui.adapter.match.MatchModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HighlightBottomSheetDialog: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentHighlightBinding
    private var simpleExoPlayer: ExoPlayer? = null

    private val viewModel: MatchesViewModel by lazy {
        ViewModelProvider(requireActivity())[MatchesViewModel::class.java]
    }

    @androidx.media3.common.util.UnstableApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHighlightBinding.inflate(inflater, container, false)
        subscribeEvents()
        return binding.root
    }

    @androidx.media3.common.util.UnstableApi
    private fun subscribeEvents() {
        viewModel.matchHighlight.observe(viewLifecycleOwner, this::populateHighlight)
    }

    @androidx.media3.common.util.UnstableApi
    private fun populateHighlight(matchModel: MatchModel) {
        val videoUrl = matchModel.highlight ?: return

        binding.tvMatchTitle.text = matchModel.description

        val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(requireContext())

        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(videoUrl))

        val mediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)

        val simpleExoPlayer = ExoPlayer.Builder(requireContext())
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        simpleExoPlayer.addMediaSource(mediaSource)
        simpleExoPlayer.playWhenReady = true

        binding.playerView.player = simpleExoPlayer
        binding.playerView.requestFocus()
    }

    private fun releasePlayer() {
        simpleExoPlayer?.release()
        simpleExoPlayer = null
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    companion object {

        fun newInstance() = HighlightBottomSheetDialog()
    }
}
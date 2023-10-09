package com.example.jason

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.jason.R
import com.example.jason.ui.matches.MatchesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressWarnings("Use for android test only")
class TestActivity: AppCompatActivity() {

    val viewModel: MatchesViewModel by lazy {
        ViewModelProvider(this)[MatchesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getTeams()
        setContentView(R.layout.activity_test)
    }
}
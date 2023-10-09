package com.example.jason.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding ?: throw IllegalStateException("View destroyed!")

    open val isViewModelActivityProvider = false

    protected val viewModel: VM by lazy {
        ViewModelProvider(
            if (isViewModelActivityProvider) {
                requireActivity()
            } else {
                this
            }
        )[vmClass]
    }

    abstract val vmClass: Class<VM>

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater.invoke(inflater, container, false)

        initView()
        subscribeEvents()

        return binding.root
    }

    open fun initView() = Unit

    open fun subscribeEvents() {
        viewModel.isError.observe(viewLifecycleOwner, this::handleError)
    }

    private fun handleError(isError: Boolean) {
        Toast.makeText(requireContext(), "Something went wrong! Please check your connection and try again!", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
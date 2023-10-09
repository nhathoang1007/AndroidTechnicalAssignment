package com.example.jason.ui.componets

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.jason.databinding.ViewBannerBinding

class BannerViewManager constructor(var anchor: View?) {

    private var msgModel: Pair<String, String>? = null
        set(value) {
            field = value
            updateSnackBar()
        }


    private lateinit var binding: ViewBannerBinding
    private lateinit var snackBarViewGroup: ViewGroup

    private var isDismissing = false
    private val dismissSnackBarHandler = Handler(Looper.getMainLooper())
    private val dismissSnackBarRunner = this::dismissSnackBarWithAnimation

    init {
        initSnackBar()
    }

    /**
     * Add snack bar view to the root screen view group
     */
    private fun initSnackBar() {
        val parentView = findSuitableParent(anchor) ?: return
        if (!isAnchorViewInitialized()) {
            snackBarViewGroup = parentView
        }

        binding = ViewBannerBinding.inflate(
            LayoutInflater.from(parentView.context),
            parentView,
            true
        )

        binding.bannerView.setOnClickListener {
            dismissSnackBarWithAnimation()
        }
    }

    private fun updateSnackBar() {
        if (this::binding.isInitialized.not()) return

        binding.tvMatchTime.text = msgModel?.first
        binding.tvMatchName.text = msgModel?.second

        resetAutoDismiss()
    }

    /**
     * To start snack bar from bottom
     * We can create CustomSnackBar instance and call show({anchor})
     * Anchor view is the root view of component like activity xml, fragment xml
     * or if you use ViewDataBinding you can use binding.root
     */
    fun show(model: Pair<String, String>) {
        if (model.first.isEmpty() || this::binding.isInitialized.not()) return

        msgModel = model

        // Hide the snack bar from the bottom layout and start slide up as beginning
        createAnimation()
    }


    /**
     * Hide snack bar view to the bellow of bottom point
     * Start translate to show up snack bar and fade animate to make snack bar visible
     */
    private fun createAnimation() {
        with(binding.bannerView) {
            alpha = 0.0f
            animate().alpha(1f).setDuration(FADE_ANIMATE_DURATION).start()
        }
    }

    /**
     * duration == 0 -> No auto dismiss
     * duration > 0 -> snack bar dismiss after duration
     */
    private fun resetAutoDismiss() {
        dismissSnackBarHandler.removeCallbacks(dismissSnackBarRunner)
        dismissSnackBarHandler.postDelayed(dismissSnackBarRunner, DELAY_DISMISS)
    }

    /**
     * Dismiss snack bar will be animated to slide down and fade out after {FADE_ANIMATE_DELAY}
     */
    private fun dismissSnackBarWithAnimation() {
        if (this::binding.isInitialized.not()) return

        with(binding.bannerView) {
            isDismissing = true
            animate().alpha(0.0f).setDuration(FADE_ANIMATE_DURATION)
                .withEndAction { dismiss() }
                .start()
        }
    }


    /**
     * After snack bar completely hidden
     * Remove the snack bar view from screen root view
     * and invoke dismiss callback
     */
    private fun dismiss() {
        if (this::binding.isInitialized.not()) return

        Handler(Looper.getMainLooper()).post {
            if (!isAnchorViewInitialized()) {
                return@post
            }
            snackBarViewGroup.removeView(binding.root)
            isDismissing = false
        }
    }

    private fun findSuitableParent(anchor: View?): ViewGroup? {
        var view: View? = anchor
        var fallback: ViewGroup? = null
        do {
            if (view is CoordinatorLayout) {
                // We've found a CoordinatorLayout, use it
                return view
            } else if (view is FrameLayout) {
                fallback = if (view.getId() == android.R.id.content) {
                    // If we've hit the decor content view, then we didn't find a CoL in the
                    // hierarchy, so use it.
                    return view
                } else {
                    // It's not the content view but we'll use it as our fallback
                    view
                }
            }
            if (view != null) {
                // Else, we will loop and crawl up the view hierarchy and try to find a parent
                val parent = view.parent
                view = if (parent is View) parent else null
            }
        } while (view != null)

        // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
        return fallback
    }

    private fun isAnchorViewInitialized() = ::snackBarViewGroup.isInitialized

    companion object {

        private const val FADE_ANIMATE_DURATION = 500L
        private const val DELAY_DISMISS = 10*1000L
    }
}
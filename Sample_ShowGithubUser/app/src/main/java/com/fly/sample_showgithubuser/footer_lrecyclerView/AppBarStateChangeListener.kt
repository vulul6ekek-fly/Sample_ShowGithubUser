package com.fly.sample_showgithubuser.footer_lrecyclerView

import com.google.android.material.appbar.AppBarLayout

abstract class AppBarStateChangeListener: AppBarLayout.OnOffsetChangedListener {

    private var mCurrentState = State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        mCurrentState = if (verticalOffset == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout,
                    State.EXPANDED
                )
            }
            State.EXPANDED
        } else if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout,
                    State.COLLAPSED
                )
            }
            State.COLLAPSED
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout,
                    State.IDLE
                )
            }
            State.IDLE
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: State)

    enum class State {
        EXPANDED, COLLAPSED, IDLE
    }
}
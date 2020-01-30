package com.ad4th.seoulandroid.ui.navigation

import android.content.Context
import android.view.MotionEvent
import android.text.method.Touch.onTouchEvent
import android.support.v4.view.ViewPager
import android.util.AttributeSet


class NoSwipeViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    var pagingEnabled: Boolean = false

    init {
     //   this.pagingEnabled = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this.pagingEnabled) {
            super.onTouchEvent(event)
        } else false

    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.pagingEnabled) {
            super.onInterceptTouchEvent(event)
        } else false

    }

}


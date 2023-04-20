package com.test.testbanner

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Field

class SlowCLManager(context: Context) : LinearLayoutManager(context, HORIZONTAL, false) {

    private val milliSecondsPerInch = 100f

    init {
        try {
            val recyclerViewSmoothScrollerSlow: Field =
                RecyclerView.LayoutManager::class.java.getDeclaredField("mSmoothScroller")
            recyclerViewSmoothScrollerSlow.isAccessible = true
            recyclerViewSmoothScrollerSlow.set(this, object : LinearSmoothScroller(context) {
                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                    return milliSecondsPerInch / displayMetrics.densityDpi
                }
            })
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
    }
}
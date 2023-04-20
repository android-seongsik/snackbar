package com.test.testbanner

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class CustomSnackBar(view: View, private val message: String, private val date: String) {

    companion object {
        fun make(view: View, message: String, date: String) = CustomSnackBar(view, message, date)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, "", 3000)
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    private val inflater = LayoutInflater.from(context)
    private val customView: View = inflater.inflate(R.layout.dialog_reward_snackbar, snackbarLayout, false)

    private val tvSample: TextView = customView.findViewById(R.id.tvRewardCoin)
    private val tvDate: TextView = customView.findViewById(R.id.tvRewardDate)

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackbarLayout) {
            setBackgroundColor(Color.TRANSPARENT)
            removeAllViews()
            setPadding(100, 0, 100, 0)
            addView(customView, 0)

            (layoutParams as FrameLayout.LayoutParams).gravity = Gravity.TOP
        }
    }

    private fun initData() {
        tvSample.text = message
        tvDate.text = date
    }

    fun show() {
        snackbar.show()
    }
}
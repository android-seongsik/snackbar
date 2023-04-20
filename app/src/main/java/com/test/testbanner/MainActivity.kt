package com.test.testbanner

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var bt: Button
    private lateinit var bt2: Button
    private lateinit var pageInfo: TextView
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var autoScrollRunnable: Runnable
    private var isUserInteracting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bannerItems = listOf(
            BannerItem(R.drawable.basic_thumnail_01),
            BannerItem(R.drawable.basic_thumnail_02),
            BannerItem(R.drawable.basic_thumnail_03),
            BannerItem(R.drawable.basic_thumnail_04)
        )

        bt = findViewById(R.id.bt)
        bt2 = findViewById(R.id.bt2)
        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = BannerAdapter(bannerItems)

        val initialPosition = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % bannerItems.size
        viewPager.setCurrentItem(initialPosition, false)

        pageInfo = findViewById(R.id.pageInfo)
        updatePageInfo(viewPager.currentItem % bannerItems.size, bannerItems.size)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updatePageInfo(position % bannerItems.size, bannerItems.size)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                isUserInteracting = state != ViewPager2.SCROLL_STATE_IDLE

                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    handler.removeCallbacks(autoScrollRunnable)
                    handler.postDelayed(autoScrollRunnable, 3000)
                } else {
                    handler.removeCallbacks(autoScrollRunnable)
                }
            }
        })

        autoScrollRunnable = Runnable {
            if (!isUserInteracting) {
                viewPager.setCurrentItem(viewPager.currentItem + 1, true)
            }
            handler.postDelayed(autoScrollRunnable, 3000)
        }

        bt.setOnClickListener {
            showBottomSheetDialog()
        }

        bt2.setOnClickListener {
            //Snack Bar
//            val snack: Snackbar = Snackbar.make(findViewById(R.id.main), "gg", Snackbar.LENGTH_SHORT)
//            val view = snack.view
//            val params = view.layoutParams as FrameLayout.LayoutParams
//            params.gravity = Gravity.TOP
//            view.layoutParams = params
//            snack.show()

            //상단 커스텀 SnackBar
//            showCustomSnackbar(findViewById(R.id.main), "1000")

            CustomSnackBar.make(findViewById(R.id.main), "1000", "gg").show()
        }

    }

    private fun showBottomSheetDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_bottom_photo_sheet, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()

        view.findViewById<LinearLayout>(R.id.llBottomAlbum).setOnClickListener {

        }

        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetDialog.dismiss()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(autoScrollRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(autoScrollRunnable)
    }

    private fun updatePageInfo(currentPage: Int, totalPages: Int) {
        pageInfo.text = getString(R.string.page_info_format, currentPage + 1, totalPages)
    }

    private fun showCustomSnackbar(view: View, rewardCoinText: String) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customSnackbarView = inflater.inflate(R.layout.dialog_reward_snackbar, null)

        val tvRewardCoin = customSnackbarView.findViewById<TextView>(R.id.tvRewardCoin)
        tvRewardCoin.text = rewardCoinText

        val popupWindow = PopupWindow(customSnackbarView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        popupWindow.isOutsideTouchable = true
//        popupWindow.isFocusable = true
        popupWindow.animationStyle = android.R.style.Animation_Dialog
        val sideMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, resources.displayMetrics).toInt()
        val topMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36f, resources.displayMetrics).toInt()

        // 상단에 표시
        popupWindow.showAtLocation(view, Gravity.TOP, 240, 0)

        // 선택적: 자동으로 사라지게 하려면 아래 코드를 사용하세요.
        Handler(Looper.getMainLooper()).postDelayed({
            if (popupWindow.isShowing) {
                popupWindow.dismiss()
            }
        }, 3000) // 3초 후에 사라짐
    }
}
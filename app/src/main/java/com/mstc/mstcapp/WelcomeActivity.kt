package com.mstc.mstcapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html.fromHtml
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mstc.mstcapp.databinding.ActivityWelcomeBinding
import com.mstc.mstcapp.ui.WelcomeFragment
import com.mstc.mstcapp.util.Constants

private const val TAG = "WelcomeActivity"

class WelcomeActivity : AppCompatActivity() {
    private val context: Context = this

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var pagerAdapter: WelcomePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_welcome) as ActivityWelcomeBinding
        val sharedPreferences =
            context.getSharedPreferences(Constants.STC_SHARED_PREFERENCES, MODE_PRIVATE)
        pagerAdapter = WelcomePagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        addBottomDots()
        binding.next.setOnClickListener {
            if (binding.viewPager.currentItem == pagerAdapter.itemCount - 1) {
                sharedPreferences.edit().putBoolean("isFirstLaunch", false).apply()
                startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                finish()
            } else {
                ++binding.viewPager.currentItem
                addBottomDots()
            }
        }

    }

    private fun addBottomDots() {
        Log.i(TAG, "addBottomDots: ${pagerAdapter.itemCount}")
        val dots = arrayOfNulls<TextView>(pagerAdapter.itemCount)
        val colorsActive = ContextCompat.getColor(context, R.color.colorPrimary)
        val colorsInactive = ContextCompat.getColor(context, R.color.gray)
        binding.layoutDots.removeAllViews()
        for (i in dots.indices) {
            Log.d(TAG, "addBottomDots() returned: $i")
            dots[i] = TextView(this)
            dots[i]!!.text = fromHtml("&nbsp;&#8226;&nbsp")
            dots[i]!!.textSize = 25f
            dots[i]!!.setTextColor(colorsInactive)
            dots[i]!!.setPadding(5, 0, 5, 0)
            binding.layoutDots.addView(dots[i])
        }
        dots[binding.viewPager.currentItem]!!.setTextColor(colorsActive)
        dots[binding.viewPager.currentItem]!!.textSize = 40f
    }

    override fun onBackPressed() {
        if (binding.viewPager.currentItem > 0) {
            --binding.viewPager.currentItem
            addBottomDots()
        } else {
            super.onBackPressed()
        }
    }

    private inner class WelcomePagerAdapter(fa: AppCompatActivity) : FragmentStateAdapter(fa) {
        private val NUM_PAGES: Int = 3

        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment = WelcomeFragment(position)

        override fun getItemId(position: Int): Long {
            if (position != NUM_PAGES - 1) {
                binding.next.text = "NEXT"
            } else binding.next.text = "CONTINUE"

            addBottomDots()
            return super.getItemId(position)
        }
    }
}
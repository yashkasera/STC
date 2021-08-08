package com.mstc.mstcapp

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.mstc.mstcapp.databinding.ActivityViewResourceBinding
import com.mstc.mstcapp.model.Domain
import com.mstc.mstcapp.ui.resources.ViewPagerAdapter
import java.util.*

class ViewResourceActivity : AppCompatActivity() {
    private val context: Context = this
    lateinit var binding: ActivityViewResourceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val domain: Domain? = intent.getSerializableExtra("domain") as Domain?
        if (domain == null) {
            Toast.makeText(context,
                "Could not load domain! Please try again...",
                Toast.LENGTH_SHORT).show()
            finish()
        } else {
            setTheme(domain.style)
            binding = DataBindingUtil.setContentView(this,
                R.layout.activity_view_resource) as ActivityViewResourceBinding
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(ContextCompat.getDrawable(context,
                R.drawable.ic_back))

            binding.toolbarTitle.text = domain.domain.uppercase(Locale.getDefault())
            binding.toolbarDescription.setText(R.string.choose_domain_helper_text)
            binding.toolbarImage.setImageDrawable(ContextCompat.getDrawable(context,
                domain.drawable))
            val viewPagerAdapter =
                ViewPagerAdapter(supportFragmentManager,
                    domain.domain.lowercase(Locale.getDefault()))
            binding.viewPager.adapter = viewPagerAdapter
            binding.tabLayout.setupWithViewPager(binding.viewPager)
            val collapsingToolbarLayout =
                findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayout)
            collapsingToolbarLayout.title = domain.domain.uppercase(Locale.getDefault())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return true
    }
}
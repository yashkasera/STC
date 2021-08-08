package com.mstc.mstcapp

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.google.android.material.chip.Chip
import com.mstc.mstcapp.databinding.ActivityMainBinding
import com.mstc.mstcapp.ui.ProjectIdeaFragment
import com.mstc.mstcapp.util.Constants
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var isHome = false
    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        binding.apply {
            drawerLayout.findViewById<View>(R.id.share)
                .setOnClickListener { share() }
            drawerLayout.findViewById<View>(R.id.feedback)
                .setOnClickListener { openURL("market://details?id=" + context.packageName) }
            drawerLayout.findViewById<View>(R.id.idea)
                .setOnClickListener { openIdeaDialog() }
            drawerLayout.findViewById<View>(R.id.instagram)
                .setOnClickListener { openURL(Constants.INSTAGRAM_URL) }
            drawerLayout.findViewById<View>(R.id.facebook)
                .setOnClickListener { openURL(Constants.FACEBOOK_URL) }
            drawerLayout.findViewById<View>(R.id.linkedin)
                .setOnClickListener { openURL(Constants.LINKEDIN_URL) }
            drawerLayout.findViewById<View>(R.id.github)
                .setOnClickListener { openURL(Constants.GITHUB_URL) }
            drawerLayout.findViewById<View>(R.id.privacy_policy)
                .setOnClickListener { openURL(Constants.PRIVACY_URL) }
        }

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        binding.drawerLayout.setDrawerListener(toggle)
        toggle.syncState()
        binding.toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_navigation)

        binding.home.setOnClickListener {
//            if (isHome) MainActivity.feed_position = 0
            selectTab(binding.home)
            navController.popBackStack()
            navController.navigate(R.id.navigation_home)
        }

        binding.resources.setOnClickListener {
            selectTab(binding.resources)
            navController.popBackStack()
            navController.navigate(R.id.navigation_resources)
        }

        binding.explore.setOnClickListener {
            selectTab(binding.explore)
            navController.popBackStack()
            navController.navigate(R.id.navigation_explore)
        }

    }

    private fun selectTab(chip: Chip) {
        for (j in arrayOf(binding.home, binding.resources, binding.explore)) {
            if (j != chip) setUnselected(j)
        }
        setSelected(chip)
    }

    private fun setUnselected(chip: Chip) {
        chip.chipBackgroundColor =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
        chip.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary))
        chip.chipIconTint =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.textColorPrimary))
    }

    private fun setSelected(chip: Chip) {
        chip.chipBackgroundColor =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorTertiaryBlue))
        chip.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        chip.chipIconTint =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary))
    }

    private fun share() {
        val intent = Intent(Intent.ACTION_SEND)
        val rand = Random()
        val messages =
            arrayOf("Are you someone who still cannot find the material to start your journey to become a proficient developer?\n",
                "On the lookout for study material?\n",
                "Budding developers! Still on the lookout for study material?\n"
            )
        val message = """
            ${messages[rand.nextInt(3)]}
            Don’t worry STC has got you covered!
            
            Download the STC app for latest resources and guidelines from 
            ${Constants.PLAY_STORE_URL}
            
            Guess what, it is FREE!
            """.trimIndent()
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(Intent.createChooser(intent, "Share Using"))
    }

    private fun openIdeaDialog() {
        val fm = supportFragmentManager
        val projectIdeaFragment: ProjectIdeaFragment = ProjectIdeaFragment.newInstance()
        projectIdeaFragment.show(fm, "projectFragment")
    }

    private fun openURL(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }


    companion object {
        private val fetchedData = HashMap<String, Boolean>()
        public var feedPosition = 0
        fun isFetchedData(domain: String): Boolean? {
            return if (fetchedData.containsKey(domain))
                fetchedData[domain]
            else false
        }
        fun setFetchedData(domain: String){
            fetchedData[domain] = true
        }
    }
}
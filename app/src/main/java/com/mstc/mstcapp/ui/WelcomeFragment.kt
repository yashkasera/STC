package com.mstc.mstcapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.text.HtmlCompat.fromHtml
import androidx.fragment.app.Fragment
import com.mstc.mstcapp.R
import com.mstc.mstcapp.WelcomeActivity
import com.mstc.mstcapp.databinding.FragmentWelcomeBinding

class WelcomeFragment(val position: Int) : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding
    private val images = intArrayOf(
        R.drawable.ic_onboarding_1,
        R.drawable.ic_onboarding_2,
        R.drawable.ic_onboarding_3
    )
    private val texts = intArrayOf(R.string.onboarding1, R.string.onboarding2, R.string.onboarding3)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView.text = getString(texts[position])
        binding.imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(),
            images[position]))
    }
}

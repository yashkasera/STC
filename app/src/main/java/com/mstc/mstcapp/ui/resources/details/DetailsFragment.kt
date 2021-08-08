package com.mstc.mstcapp.ui.resources.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mstc.mstcapp.databinding.FragmentDetailsBinding

private const val TAG = "DetailsFragment"

class DetailsFragment(val domain: String) : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        viewModel.getDetails(domain).observe(viewLifecycleOwner, { detail ->
            run {
                binding.apply {
                    if (detail != null) {
                        retryButton.visibility = View.GONE
                        details.text = detail.description
                        salary.text = detail.expectation
                    } else {
                        retryButton.visibility = View.VISIBLE
                    }
                }
            }
        })
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshDetails(domain)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
}
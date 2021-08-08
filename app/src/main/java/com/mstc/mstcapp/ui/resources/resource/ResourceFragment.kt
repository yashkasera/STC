package com.mstc.mstcapp.ui.resources.resource

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mstc.mstcapp.databinding.FragmentSwipeRecyclerBinding

class ResourceFragment(domain: String) : Fragment() {

    var domain = domain
    private lateinit var binding: FragmentSwipeRecyclerBinding
    private lateinit var viewModel: ResourceViewModel
    private lateinit var resourceAdapter: ResourceAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSwipeRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ResourceViewModel::class.java)
        resourceAdapter = ResourceAdapter()
        binding.recyclerView.adapter = resourceAdapter
        viewModel.getResources(requireContext(), domain)
            .observe(viewLifecycleOwner, { resources ->
                run {
                    if (resources.isEmpty())
                        binding.retryButton.isVisible = true
                    else {
                        binding.retryButton.isVisible = false
                        resourceAdapter.list = resources
                    }
                }
            })
        binding.retryButton.setOnClickListener {
            viewModel.refreshResources(requireContext(),
                domain)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshResources(requireContext(), domain)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

}
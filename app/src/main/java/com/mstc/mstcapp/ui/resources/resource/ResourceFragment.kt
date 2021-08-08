package com.mstc.mstcapp.ui.resources.resource

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mstc.mstcapp.databinding.FragmentSwipeRecyclerBinding

class ResourceFragment(val domain: String) : Fragment() {

    private lateinit var binding: FragmentSwipeRecyclerBinding
    private lateinit var viewModel: ResourceViewModel
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
        val resourceAdapter = ResourceAdapter()
        binding.recyclerView.adapter = resourceAdapter
        viewModel.getResources(domain)
            .observe(viewLifecycleOwner, { resources ->
                run {
                    if (resources == null || resources.isEmpty()) {
                        binding.retryButton.visibility = View.VISIBLE
                    } else {
                        binding.retryButton.visibility = View.GONE
                        resourceAdapter.list = resources
                    }
                }
            })
        binding.retryButton.setOnClickListener {
            viewModel.refreshResources(domain)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshResources(domain)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

}
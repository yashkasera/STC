package com.mstc.mstcapp.ui.explore.about

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.mstc.mstcapp.databinding.FragmentSwipeRecyclerBinding

class AboutFragment : Fragment() {

    companion object {
        fun newInstance() = AboutFragment()
    }

    private lateinit var viewModel: AboutViewModel
    private lateinit var binding: FragmentSwipeRecyclerBinding
    private lateinit var boardMemberAdapter: BoardMemberAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSwipeRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)
        boardMemberAdapter = BoardMemberAdapter()
        binding.recyclerView.adapter = boardMemberAdapter
        viewModel.getBoard(requireContext())
            .observe(viewLifecycleOwner, { resources ->
                run {
                    if (resources.isEmpty())
                        binding.retryButton.isVisible = true
                    else {
                        binding.retryButton.isVisible = false
                        boardMemberAdapter.list = resources
                    }
                }
            })
        binding.retryButton.setOnClickListener {
            viewModel.refreshBoard(requireContext())
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshBoard(requireContext())
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

}
package com.mstc.mstcapp.ui.resources.roadmap

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mstc.mstcapp.R
import com.mstc.mstcapp.databinding.FragmentRoadmapBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RoadmapFragment(domain: String) : Fragment() {
    var domain: String = domain
    private lateinit var binding: FragmentRoadmapBinding
    private lateinit var viewModel: RoadmapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRoadmapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RoadmapViewModel::class.java)
        viewModel.getRoadmap(requireContext(), domain)
            .observe(viewLifecycleOwner, { roadmap ->
                run { setImage(roadmap.image) }
            })
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshRoadmap(requireContext(), domain)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setImage(base64: String) = runBlocking {
        launch(Dispatchers.Default) {
            binding.roadmapImage.post {
                try {
                    val decodedString: ByteArray =
                        Base64.decode(base64,
                            Base64.DEFAULT)
                    val picture =
                        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    binding.roadmapImage.setImageBitmap(picture)
                } catch (e: Exception) {
                    binding.roadmapImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                        R.drawable.ic_error))
                    e.printStackTrace()
                }
            }
        }
    }
}
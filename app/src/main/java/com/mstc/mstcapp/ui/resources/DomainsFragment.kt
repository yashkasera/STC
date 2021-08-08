package com.mstc.mstcapp.ui.resources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionInflater
import com.mstc.mstcapp.R
import com.mstc.mstcapp.databinding.FragmentResourcesBinding
import com.mstc.mstcapp.model.Domain
import com.mstc.mstcapp.util.ClickListener
import com.mstc.mstcapp.util.RecyclerTouchListener
import java.util.*

class DomainsFragment : Fragment() {
    private var list: ArrayList<Domain>? = null

    lateinit var binding: FragmentResourcesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fade)
        exitTransition = inflater.inflateTransition(R.transition.fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentResourcesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        list = ArrayList<Domain>()
        val style1: Int = R.style.resources_red
        val style2: Int = R.style.resources_blue
        val style3: Int = R.style.resources_yellow

        /* ADD A NEW RESOURCE HERE */
        list!!.add(Domain("Android", R.drawable.ic_app_dev, style1))
        list!!.add(Domain("Frontend", R.drawable.ic_frontend, style2))
        list!!.add(Domain("Backend", R.drawable.ic_backend, style3))
        list!!.add(Domain("Design", R.drawable.ic_design, style1))
        list!!.add(Domain("Machine Learning", R.drawable.ic_ml, style2))
        list!!.add(Domain("Competitive Coding", R.drawable.ic_cc, style3))
        val domainAdapter = DomainAdapter()
        domainAdapter.list = list as ArrayList<Domain>
        binding.recyclerView.adapter = domainAdapter
        binding.recyclerView.addOnItemTouchListener(RecyclerTouchListener(context,
            binding.recyclerView,
            object : ClickListener {
                override fun onClick(view: View?, position: Int) {
                    viewResource(list!![position])
                }
                override fun onLongClick(view: View?, position: Int) {}
            }))
    }

    private fun viewResource(Domain: Domain) {
        val bundle = Bundle()
        bundle.putSerializable("domain", Domain)
        NavHostFragment.findNavController(this@DomainsFragment)
            .navigate(R.id.action_navigation_resources_to_navigation_view_resource_activity, bundle)
    }
}
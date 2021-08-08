package com.mstc.mstcapp.ui.explore.project

import android.util.Log
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mstc.mstcapp.R
import com.mstc.mstcapp.model.explore.Project

private const val TAG = "ProjectAdapter"

class ProjectAdapter : PagingDataAdapter<Project, RecyclerView.ViewHolder>(UIMODEL_COMPARATOR) {

    companion object {
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<Project>() {
            override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean =
                oldItem.title == newItem.title
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_feed
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val feed = getItem(position)
        if (feed != null) {
            (holder as ProjectViewHolder).bind(feed)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProjectViewHolder.create(parent)
    }
}


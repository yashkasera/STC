package com.mstc.mstcapp.ui.resources.resource

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mstc.mstcapp.R
import com.mstc.mstcapp.model.resource.Resource
import com.mstc.mstcapp.ui.home.FeedViewHolder
import com.mstc.mstcapp.util.Constants

class ResourceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val mView = view
    private val title: TextView = view.findViewById(R.id.title)
    private val description: TextView = view.findViewById(R.id.description)
    private val share: ImageButton = view.findViewById(R.id.share)
    fun bind(resource: Resource) {
        title.text = resource.title
        description.text = resource.description
        share.setOnClickListener { shareResource(resource.title, resource.link) }
        mView.setOnClickListener { openURL(resource.link) }
    }

    private fun openURL(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        mView.context.startActivity(intent)
    }

    private fun shareResource(title: String, link: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, """
     $link

     Here is an article for you! This is an article on $title.
     To keep receiving amazing articles like these, check out the STC app here 
     
     ${Constants.PLAY_STORE_URL}
     """.trimIndent())
        mView.context.startActivity(Intent.createChooser(intent, "Share Using"))
    }
    companion object {
        fun create(parent: ViewGroup): ResourceViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_resource, parent, false)
            return ResourceViewHolder(view)
        }
    }
}

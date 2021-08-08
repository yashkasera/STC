package com.mstc.mstcapp.ui.resources

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.mstc.mstcapp.R
import com.mstc.mstcapp.model.Domain
import com.mstc.mstcapp.ui.home.FeedViewHolder

class DomainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var background: RelativeLayout = itemView.findViewById(R.id.background)
    var image: ImageView = itemView.findViewById(R.id.image)
    var textView: TextView = itemView.findViewById(R.id.domain)

    fun bind(item: Domain) {
        val context = itemView.context
        textView.text = item.domain
        image.setImageDrawable((ContextCompat.getDrawable(context, item.drawable)))
        val drawable = ResourcesCompat.getDrawable(context.resources,
            R.drawable.resource_background,
            ContextThemeWrapper(context, item.style).theme)
        background.background = drawable
    }
    companion object {
        fun create(parent: ViewGroup): DomainViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_domain, parent, false)
            return DomainViewHolder(view)
        }
    }
}
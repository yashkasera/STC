package com.mstc.mstcapp.ui.explore.about

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mstc.mstcapp.R
import com.mstc.mstcapp.model.explore.BoardMember
import com.mstc.mstcapp.ui.home.FeedViewHolder

class BoardMemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val mView = view
    private val image = view.findViewById<ImageView?>(R.id.image)
    private val name by lazy { view.findViewById<TextView?>(R.id.name) }
    private val position = view.findViewById<TextView?>(R.id.position)
    private val phrase = view.findViewById<TextView?>(R.id.phrase)
    fun bind(boardMember: BoardMember) {
        name.text = boardMember.name
        position.text = boardMember.position
        phrase.text = boardMember.phrase
        mView.setOnClickListener { openURL(boardMember.link) }
        Glide.with(mView.context)
            .load(boardMember.photo)
            .into(image)
    }

    private fun openURL(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        mView.context.startActivity(intent)
    }

    companion object {
        fun create(parent: ViewGroup): BoardMemberViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_board_member, parent, false)
            return BoardMemberViewHolder(view)
        }
    }

}

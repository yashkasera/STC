package com.mstc.mstcapp.ui.home

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mstc.mstcapp.R
import com.mstc.mstcapp.model.Feed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


private const val TAG = "FeedViewHolder"

class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val MAX_LINES = 3

    private val mView: View = view
    private val title: TextView = view.findViewById(R.id.title)
    private val description: TextView = view.findViewById(R.id.description)
    private val image: ImageView = view.findViewById(R.id.image)
    private val cardView: CardView = view.findViewById(R.id.cardView)

    fun bind(feed: Feed) {
        title.text = feed.title
        description.post {
            run {
                description.text = feed.description
                if (description.lineCount > MAX_LINES) {
                    feed.expand = true
                    collapseDescription(feed)
                }
            }
        }
        cardView.setOnClickListener {
            description.text = feed.description
            if (description.lineCount > MAX_LINES) {
                if (feed.expand) collapseDescription(feed)
                else expandDescription(feed)
            } else {
                Log.i(TAG, "bind: does not exceed limit")
            }
        }
        loadImage(feed)
        image.setOnClickListener { openURL(feed.link) }
        cardView.setBackgroundColor(ContextCompat.getColor(mView.context,
            when {
                position % 3 == 0 -> R.color.colorTertiaryBlue
                position % 3 == 1 -> R.color.colorTertiaryRed
                else -> R.color.colorTertiaryYellow
            }
        ))
    }

    private fun collapseDescription(feed: Feed) {
        description.post {
            run {
                val lastCharShown: Int =
                    description.layout.getLineVisibleEnd(MAX_LINES - 1)
                description.maxLines = MAX_LINES
                val moreString = "Read More"
                val actionDisplayText: String =
                    feed.description.substring(0,
                        lastCharShown - "  $moreString".length - 3) + "...  $moreString"
                val truncatedSpannableString = SpannableString(actionDisplayText)
                val startIndex = actionDisplayText.indexOf(moreString)
                truncatedSpannableString.setSpan(ForegroundColorSpan(mView.context.getColor(
                    R.color.colorPrimary)),
                    startIndex,
                    startIndex + moreString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                description.text = truncatedSpannableString
            }
        }
        feed.expand = false
    }

    private fun expandDescription(feed: Feed) {
        description.post {
            run {
                val suffix = "Read Less"
                description.maxLines = 100
                val actionDisplayText =
                    "${feed.description}  $suffix"
                val truncatedSpannableString = SpannableString(actionDisplayText)
                val startIndex = actionDisplayText.indexOf(suffix)
                truncatedSpannableString.setSpan(ForegroundColorSpan(mView.context.getColor(
                    R.color.colorPrimary)),
                    startIndex,
                    startIndex + suffix.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                description.text = truncatedSpannableString
            }
        }
        feed.expand = true
    }

    private fun loadImage(feed: Feed) = runBlocking {
        launch(Dispatchers.Default) {
            image.post {
                try {
                    val decodedString: ByteArray =
                        Base64.decode(feed.image,
                            Base64.DEFAULT)
                    val picture =
                        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    image.setImageBitmap(picture)
                } catch (e: Exception) {
                    image.setImageDrawable(ContextCompat.getDrawable(mView.context,
                        R.drawable.ic_error))
                    e.printStackTrace()
                }
            }
        }
    }

    private fun openURL(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        mView.context.startActivity(intent)
    }

    companion object {
        fun create(parent: ViewGroup): FeedViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_feed, parent, false)
            return FeedViewHolder(view)
        }
    }

}
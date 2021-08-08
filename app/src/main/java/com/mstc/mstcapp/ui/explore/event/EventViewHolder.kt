package com.mstc.mstcapp.ui.explore.event

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
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
import com.mstc.mstcapp.model.explore.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val TAG = "EventViewHolder"

class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val mView: View = view
    private val title: TextView = view.findViewById(R.id.title)
    private val description: TextView = view.findViewById(R.id.description)
    private val image: ImageView = view.findViewById(R.id.image)
    private val cardView: CardView = view.findViewById(R.id.cardView)
    private val status: TextView = view.findViewById(R.id.status)

    fun bind(event: Event) = runBlocking {
        Log.d(TAG, "bind() returned: ${event.id} => ${event.title}")
        title.text = event.title
        description.text = event.description
        status.text = event.status
        status.setTextColor(ContextCompat.getColor(mView.context,
            when (event.status) {
                "UPCOMING" -> R.color.colorSecondaryBlue
                "ONGOING" -> R.color.colorSecondaryYellow
                else -> R.color.colorSecondaryRed
            }))
        loadImage(event.image)
        mView.setOnClickListener { openURL(event.link) }
        cardView.setBackgroundColor(ContextCompat.getColor(mView.context,
            when {
                position % 3 == 0 -> R.color.colorTertiaryBlue
                position % 3 == 1 -> R.color.colorTertiaryRed
                else -> R.color.colorTertiaryYellow
            }
        ))
    }

    private suspend fun loadImage(base64: String?) = coroutineScope {
        launch(Dispatchers.Default) {
            image.post {
                try {
                    val decodedString: ByteArray =
                        Base64.decode(base64,
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
        fun create(parent: ViewGroup): EventViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_event, parent, false)
            return EventViewHolder(view)
        }
    }

}
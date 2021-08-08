package com.mstc.mstcapp.ui.explore.project

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mstc.mstcapp.R
import com.mstc.mstcapp.model.explore.Project
import com.mstc.mstcapp.util.ImageWrap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


private const val TAG = "ProjectViewHolder"

class ProjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val mView: View = view
    private val title: TextView = view.findViewById(R.id.title)
    private val details: TextView = view.findViewById(R.id.details)
    private val image: ImageView = view.findViewById(R.id.image)
    private val cardView: CardView = view.findViewById(R.id.cardView)
    private val viewMore: ImageButton = view.findViewById(R.id.viewMore)
    var finalHeight: Int = 0
    var finalWidth: Int = 0

    fun bind(project: Project) = runBlocking {
        title.text = project.title
        details.post {
            run {
                details.text = project.description
                if (details.lineCount < 3)
                    viewMore.visibility = View.GONE
                else
                    viewMore.visibility = View.VISIBLE
            }
        }
        viewMore.setOnClickListener {
            project.expand = !project.expand
            viewMore.setImageDrawable(ContextCompat.getDrawable(mView.context,
                if (!project.expand) R.drawable.ic_baseline_keyboard_arrow_down_24
                else R.drawable.ic_baseline_keyboard_arrow_up_24
            ))
            makeSpan(project)
        }
        loadImage(project.image)
        val vto: ViewTreeObserver = image.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                image.post {
                    run {
                        image.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        finalHeight = image.measuredHeight
                        finalWidth = image.measuredWidth
                        makeSpan(project)
                    }
                }
            }
        })

        mView.setOnClickListener { openURL(project.link) }
        cardView.setCardBackgroundColor(
            ContextCompat.getColor(mView.context, when {
                layoutPosition % 3 == 0 -> R.color.colorTertiaryBlue
                layoutPosition % 3 == 1 -> R.color.colorTertiaryRed
                else -> R.color.colorTertiaryYellow
            }))
    }

    private fun makeSpan(project: Project) {
        var text = project.description
        details.text = text
        if (details.lineCount > 3 && !project.expand) {
            val lastCharShown: Int =
                details.layout.getLineVisibleEnd(1)
            text = project.description.substring(0, lastCharShown) + "..."
        }
        val mSpannableString = SpannableString(text)
        val allTextStart = 0
        val allTextEnd = text.length - 1

        val lines: Int
        val bounds = Rect()

        details.paint.getTextBounds(text.substring(0, 10), 0, 1, bounds)
        val fontSpacing: Float = details.paint.fontSpacing
        lines = (finalHeight / fontSpacing).toInt()

        val span = ImageWrap(lines - 1, finalWidth + 16)
        mSpannableString.setSpan(span, allTextStart, allTextEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        details.text = mSpannableString

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
        fun create(parent: ViewGroup): ProjectViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_project, parent, false)
            return ProjectViewHolder(view)
        }
    }
}
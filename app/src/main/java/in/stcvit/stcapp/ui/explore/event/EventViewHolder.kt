package `in`.stcvit.stcapp.ui.explore.event

import `in`.stcvit.stcapp.R
import `in`.stcvit.stcapp.databinding.ItemEventBinding
import `in`.stcvit.stcapp.model.explore.Event
import `in`.stcvit.stcapp.util.Functions
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView


private const val TAG = "EventViewHolder"

class EventViewHolder(
    private val binding: ItemEventBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(event: Event) {
        binding.apply {
            description.text = event.description
            title.text = event.title
            makeSpan(event)
            loadImage(event)
            image.setOnClickListener { Functions.openLinkWithAnimation(root, event.link) }
            title.setOnClickListener { Functions.openLinkWithAnimation(root, event.link) }
            constraintLayout.setOnClickListener {
                Functions.openLinkWithAnimation(
                    root,
                    event.link
                )
            }
            reminder.text = event.reminder
            reminder.isVisible = event.reminder != null

            reminder.setOnClickListener {
                Log.i(TAG, "bind: ${event.startDate}")
                val intent = Intent(Intent.ACTION_EDIT)
                intent.type = "vnd.android.cursor.item/event"
                intent.putExtra("allDay", false)
                intent.putExtra("beginTime", Functions.timestampToEpochSeconds(event.startDate))
                intent.putExtra("endTime", Functions.timestampToEpochSeconds(event.endDate))
                intent.putExtra("title", event.title)
                intent.putExtra("description", event.description)
                try {
                    root.context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(root.context, "Cannot create a reminder!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            root.apply {
                setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        when (layoutPosition % 3) {
                            0 -> R.color.colorTertiaryBlue
                            1 -> R.color.colorTertiaryRed
                            else -> R.color.colorTertiaryYellow
                        }
                    )
                )
            }
            status.text = event.status
            status.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    root.context,
                    when (event.status) {
                        "UPCOMING" -> R.color.green
                        "ONGOING" -> R.color.colorSecondaryYellow
                        else -> R.color.red
                    }
                )
            )
        }
    }

    private fun ItemEventBinding.makeSpan(event: Event) {
        var text = event.description
        description.post {
            description.text = text
            if (description.lineCount > 3 && !event.expand) {
                val lastCharShown: Int =
                    description.layout.getLineVisibleEnd(1)
                text = event.description.substring(0, lastCharShown) + "…more"
            } else if (event.expand) {
                text = event.description + "…View Less"
            }
            val spannableString = SpannableString(text)
            val extra = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Functions.openLinkWithAnimation(root, event.link)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = ContextCompat.getColor(root.context, R.color.textColorPrimary)
                }
            }
            val more = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    event.expand = !event.expand
                    makeSpan(event)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = ContextCompat.getColor(root.context, R.color.gray)
                    ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
            }
            val allTextStart = 0
            val allTextEnd = text.length
            if (text.contains("…")) {
                spannableString.setSpan(
                    more,
                    text.indexOf("…") + 1,
                    allTextEnd,
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                )
                spannableString.setSpan(
                    extra,
                    allTextStart,
                    text.indexOf("…") + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else
                spannableString.setSpan(
                    extra,
                    allTextStart,
                    allTextEnd,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            description.text = spannableString
            description.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun ItemEventBinding.loadImage(event: Event) {
        try {
            image.post {
                val decodedString: ByteArray =
                    Base64.decode(
                        event.image,
                        Base64.DEFAULT
                    )
                val picture =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                image.setImageBitmap(picture)
            }
        } catch (e: Exception) {
            image.setImageDrawable(
                ContextCompat.getDrawable(
                    image.context,
                    R.drawable.ic_error
                )
            )
            e.printStackTrace()
        }
    }

    companion object {
        fun create(parent: ViewGroup): EventViewHolder {
            val binding =
                ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return EventViewHolder(binding)
        }
    }

}
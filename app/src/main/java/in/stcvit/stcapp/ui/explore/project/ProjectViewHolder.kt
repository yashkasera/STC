package `in`.stcvit.stcapp.ui.explore.project

import `in`.stcvit.stcapp.R
import `in`.stcvit.stcapp.databinding.ItemProjectBinding
import `in`.stcvit.stcapp.model.explore.Project
import `in`.stcvit.stcapp.util.Functions
import `in`.stcvit.stcapp.util.ImageWrap
import android.graphics.Rect
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


private const val TAG = "ProjectViewHolder"

class ProjectViewHolder(
    private val binding: ItemProjectBinding
) : RecyclerView.ViewHolder(binding.root) {
    var finalHeight: Int = 0
    var finalWidth: Int = 0

    fun bind(project: Project) {
        binding.apply {
            title.text = project.title

            details.post {
                run { details.text = project.description }
            }

            Glide.with(root.context)
                .load(project.image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.loading_placeholder)
                .fallback(R.drawable.ic_error)
                .into(image)

            image.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
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
            image.clipToOutline = true
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
            relativeLayout.setOnClickListener {
                Functions.openLinkWithAnimation(
                    relativeLayout,
                    project.link
                )
            }
        }
    }

    private fun ItemProjectBinding.makeSpan(project: Project) {
        var text = project.description
        details.text = text
        if (details.lineCount > 3 && !project.expand) {
            val lastCharShown: Int =
                details.layout.getLineVisibleEnd(1)
            text = project.description.substring(0, lastCharShown) + "…more"
        } else if (project.expand) {
            text = project.description + "…View Less"
        }
        val spannableString = SpannableString(text)

        val allTextStart = 0
        val allTextEnd = text.length

        val lines: Int
        val bounds = Rect()

        details.paint.getTextBounds(text.substring(0, 10), 0, 1, bounds)
        val fontSpacing: Float = details.paint.fontSpacing
        lines = (finalHeight / fontSpacing).toInt()

        val span = ImageWrap(lines - 1, finalWidth)
        val extra = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Functions.openLinkWithAnimation(relativeLayout, project.link)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        val more = object : ClickableSpan() {
            override fun onClick(widget: View) {
                project.expand = !project.expand
                makeSpan(project)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
        }
        spannableString.setSpan(
            span,
            allTextStart,
            allTextEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

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

        details.text = spannableString
        details.movementMethod = LinkMovementMethod.getInstance()
    }


    companion object {
        fun create(parent: ViewGroup): ProjectViewHolder {
            val binding =
                ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ProjectViewHolder(binding)
        }
    }
}
package com.mstc.mstcapp.util

import android.os.Build
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class Functions {
    companion object {
        fun timestampToEpochSeconds(tzDate: String?): Long {
            var epoch: Long = 0
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val instant = Instant.parse(tzDate)
                    epoch = instant.epochSecond * 1000
                } else {
                    val sdf =
                        SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSSSS'Z'", Locale.getDefault())
                    sdf.timeZone = TimeZone.getTimeZone("UTC")
                    val date = sdf.parse(tzDate)
                    if (date != null) {
                        epoch = date.time
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return epoch
        }

    }
}



package `in`.stcvit.stcapp.model.explore

import `in`.stcvit.stcapp.util.Functions
import android.text.format.DateUtils
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "EVENTS")
class Event(
    @SerializedName("title")
    val title: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("link")
    val link: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("endDate")
    val endDate: String,

    @PrimaryKey
    @SerializedName("_id")
    val id: String,
) {
    init {
        description = description
            .replace("  ", " ")
            .trimIndent()
    }

    @Ignore
    var status: String? = null

    @Ignore
    var expand: Boolean = false

    @Ignore
    var reminder: String? = null

    init {
        val startDate1 = Date(Functions.timestampToEpochSeconds(startDate))
        val endDate1 = Date(Functions.timestampToEpochSeconds(endDate))
        status = if (startDate1.after(Date()))
            "UPCOMING"
        else if (startDate1.before(Date()) && endDate1.after(Date()))
            "ONGOING"
        else
            "COMPLETED"

        if (startDate1.after(Date())) {
            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            reminder =
                "${formatDate(startDate1)} @ ${sdf.format(startDate1)} - ${sdf.format(endDate1)}"
        }
    }

    private fun formatDate(date: Date): String {
        var str = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
        if (DateUtils.isToday(date.time))
            str = "Today"
        val c1 = Calendar.getInstance()
        c1.add(Calendar.DAY_OF_YEAR, 1)
        val c2 = Calendar.getInstance()
        c2.time = date
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
            && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
        )
            str = "Tomorrow"
        return str
    }

    override fun toString(): String = "$id, $title, $description, $startDate, $endDate, $link"

}
package `in`.stcvit.stcapp.ui.home

import `in`.stcvit.stcapp.model.Feed
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

private const val TAG = "FeedAdapter"

class FeedAdapter : PagingDataAdapter<Feed, FeedViewHolder>(UIMODEL_COMPARATOR) {

    companion object {
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<Feed>() {
            override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean =
                oldItem.title == newItem.title
        }
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val feed = getItem(position)
        if (feed != null)
            holder.bind(feed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder =
        FeedViewHolder.create(parent)
}


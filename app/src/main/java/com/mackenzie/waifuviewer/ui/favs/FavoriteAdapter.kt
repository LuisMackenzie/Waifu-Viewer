package com.mackenzie.waifuviewer.ui.favs

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.ViewMediaItemBinding
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.ui.common.inflate
import com.mackenzie.waifuviewer.ui.common.loadUrlCenterCrop

class FavoriteAdapter(
    private val listener: OnItemClickListener
    ): ListAdapter<FavoriteItem, FavoriteAdapter.ViewHolder>(WaifuFavDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.view_media_item, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val waifuItem = getItem(position)
        holder.bind(waifuItem)
        holder.itemView.setOnClickListener { listener.onClick(waifuItem) }
        holder.itemView.setOnLongClickListener { listener.onLongClick(waifuItem); true }
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val binding = ViewMediaItemBinding.bind(view)
        fun bind(waifu: FavoriteItem) = with(binding) {
            waifuTitle.text = waifu.title
            waifuThumb.loadUrlCenterCrop(waifu.url)
            ivFavs.visibility = if (waifu.isFavorite) View.VISIBLE else View.GONE

            /*if (waifu.url.substringAfterLast('.') == "png") {
                preview.visibility = View.GONE
            }*/
        }
    }

    private class WaifuFavDiffCallback : DiffUtil.ItemCallback<FavoriteItem>() {
        override fun areItemsTheSame(oldItem: FavoriteItem, newItem: FavoriteItem): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: FavoriteItem, newItem: FavoriteItem): Boolean = oldItem == newItem
    }

    interface OnItemClickListener{
        fun onClick(waifu: FavoriteItem)
        fun onLongClick(waifu: FavoriteItem)
    }
}

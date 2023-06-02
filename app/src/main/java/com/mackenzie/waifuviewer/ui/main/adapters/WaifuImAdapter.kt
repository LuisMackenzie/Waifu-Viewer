package com.mackenzie.waifuviewer.ui.main.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.ViewMediaItemBinding
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.ui.common.inflate
import com.mackenzie.waifuviewer.ui.common.loadUrlCenterCrop
import com.mackenzie.waifuviewer.ui.main.ImListener

class WaifuImAdapter(
    private val listener: ImListener
): ListAdapter<WaifuImItem, WaifuImAdapter.ViewHolder>(WaifuImDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.view_media_item, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val waifuItem = getItem(position)
        holder.bind(waifuItem)
        holder.itemView.setOnClickListener { listener(waifuItem) }
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val binding = ViewMediaItemBinding.bind(view)
        fun bind(waifu: WaifuImItem) = with(binding) {
            waifuTitle.text = waifu.imageId.toString()
            waifuThumb.loadUrlCenterCrop(waifu.url)
            ivFavs.visibility = if (waifu.isFavorite) View.VISIBLE else View.GONE

            /*if (waifu.url.substringAfterLast('.') == "png") {
                preview.visibility = View.GONE
            }*/
        }
    }

    private class WaifuImDiffCallback : DiffUtil.ItemCallback<WaifuImItem>() {
        override fun areItemsTheSame(oldItem: WaifuImItem, newItem: WaifuImItem): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: WaifuImItem, newItem: WaifuImItem): Boolean = oldItem == newItem
    }
}

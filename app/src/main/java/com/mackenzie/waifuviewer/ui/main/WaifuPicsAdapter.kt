package com.mackenzie.waifuviewer.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.ViewMediaItemBinding
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.ui.common.basicDiffUtil
import com.mackenzie.waifuviewer.ui.common.inflate
import com.mackenzie.waifuviewer.ui.common.loadUrl

class WaifuPicsAdapter(private val listener: (WaifuPicItem) -> Unit ): ListAdapter<WaifuPicItem, WaifuPicsAdapter.ViewHolder>(basicDiffUtil { old, new -> old.id == new.id }) {

    val picsAdapter: WaifuPicsAdapter = this

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.view_media_item, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val waifuItem = getItem(position)
        holder.bind(waifuItem)
        holder.itemView.setOnClickListener { listener(waifuItem) }
    }

    override fun submitList(list: List<WaifuPicItem>?) {
        super.submitList(list?.toMutableList())
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val binding = ViewMediaItemBinding.bind(view)
        fun bind(waifu: WaifuPicItem) = with(binding) {
            // waifuPic = waifu
            waifuTitle.text = waifu.url.substringAfterLast('/').substringBeforeLast('.')
            waifuThumb.loadUrl(waifu.url)
            ivFavs.visibility = if (waifu.isFavorite) View.VISIBLE else View.GONE
        }

    }

}
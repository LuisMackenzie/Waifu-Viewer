package com.mackenzie.waifuviewer.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.ViewMediaItemBinding
import com.mackenzie.waifuviewer.models.db.WaifuImItem
import com.mackenzie.waifuviewer.ui.common.basicDiffUtil
import com.mackenzie.waifuviewer.ui.common.inflate
import com.mackenzie.waifuviewer.ui.common.loadUrl

class WaifuImAdapter(private val listener: (WaifuImItem) -> Unit ): RecyclerView.Adapter<WaifuImAdapter.ViewHolder>() {

    var waifuItemList: List<WaifuImItem> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.imageId == new.imageId }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.view_media_item, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val waifuItem = waifuItemList[position]
        holder.bind(waifuItem)
        holder.itemView.setOnClickListener { listener(waifuItem) }
    }

    override fun getItemCount(): Int = waifuItemList.size

    fun submitList(waifus: List<WaifuImItem>) {
        waifuItemList = waifus
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val binding = ViewMediaItemBinding.bind(view)
        fun bind(waifu: WaifuImItem) = with(binding) {
            waifuTitle.text = waifu.imageId.toString()
            waifuThumb.loadUrl(waifu.url)
        }


    }

}
package com.mackenzie.waifuviewer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mackenzie.waifuviewer.databinding.FragmentSelectorBinding
import com.mackenzie.waifuviewer.models.Tag

class TagAdapter(var tagItemList: List<Tag>, private val listener: (Tag) -> Unit ):
    RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagAdapter.ViewHolder {
        val binding = FragmentSelectorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tagItemList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item) }
    }

    override fun getItemCount(): Int = tagItemList.size

    inner class ViewHolder(private val binding: FragmentSelectorBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(tag: Tag) {
            /*binding.waifuTitle.text = waifu.image_id.toString()
            Glide.with(binding.root.context)
                .load(waifu.url)
                .into(binding.waifuThumb)*/
        }

    }

}
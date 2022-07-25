package com.mackenzie.waifuviewer.ui.main

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mackenzie.waifuviewer.data.db.WaifuImItem
import com.mackenzie.waifuviewer.data.db.WaifuPicItem

@BindingAdapter("itemsPics")
fun RecyclerView.setImItems(waifus: List<WaifuPicItem>?) {
    if (waifus != null) {
        (adapter as? WaifuPicsAdapter)?.submitList(waifus)
    }
}

@BindingAdapter("itemsIm")
fun RecyclerView.setPicItems(waifus: List<WaifuImItem>?) {
    if (waifus != null) {
        (adapter as? WaifuImAdapter)?.submitList(waifus)
    }
}
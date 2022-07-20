package com.mackenzie.waifuviewer.ui.main

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mackenzie.waifuviewer.adapters.WaifuImAdapter
import com.mackenzie.waifuviewer.adapters.WaifuPicsAdapter
import com.mackenzie.waifuviewer.models.db.WaifuImItem
import com.mackenzie.waifuviewer.models.db.WaifuPicItem

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
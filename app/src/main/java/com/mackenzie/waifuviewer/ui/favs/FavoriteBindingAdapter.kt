package com.mackenzie.waifuviewer.ui.favs

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mackenzie.waifuviewer.domain.FavoriteItem

@BindingAdapter("items")
fun RecyclerView.setItems(waifus: List<FavoriteItem>?) {
    if (waifus != null) {
        (adapter as? FavoriteAdapter)?.submitList(waifus)
    }
}
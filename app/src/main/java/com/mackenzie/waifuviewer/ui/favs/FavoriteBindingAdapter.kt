package com.mackenzie.waifuviewer.ui.favs

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.ui.main.WaifuPicsAdapter

@BindingAdapter("items")
fun RecyclerView.setItems(waifus: List<FavoriteItem>?) {
    if (waifus != null) {
        (adapter as? FavoriteAdapter)?.submitList(waifus)
    }
}
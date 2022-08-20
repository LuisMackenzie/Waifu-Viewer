package com.mackenzie.waifuviewer.ui.main

import androidx.core.view.forEach
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.domain.WaifuPicItem

@BindingAdapter("itemsPics")
fun RecyclerView.setPicsItems(waifus: List<WaifuPicItem>?) {
    if (waifus != null) {
        (adapter as? WaifuPicsAdapter)?.submitList(waifus)
    }
}

@BindingAdapter("itemsIm")
fun RecyclerView.setImItems(waifus: List<WaifuImItem>?) {
    if (waifus != null) {
        (adapter as? WaifuImAdapter)?.submitList(waifus)
    }
}

@BindingAdapter("onChooseTypeChanged")
fun ChipGroup.onChooseTypeChanged(listener: OnChooseTypeChanged?) {
    setOnCheckedChangeListener { group, checkedId ->
        group.forEach {
            val chip = it as Chip
            if (chip.id == checkedId) {
                val type = when (chip.text) {
                    context.getString(R.string.server_normal) -> ServerType.NORMAL
                    else -> ServerType.ENHANCED
                }
                listener?.onChooseTypeChanged(type)
            }
        }
    }
}

interface OnChooseTypeChanged {
    fun onChooseTypeChanged(type: ServerType)
}
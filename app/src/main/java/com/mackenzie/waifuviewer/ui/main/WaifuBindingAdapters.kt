package com.mackenzie.waifuviewer.ui.main

import androidx.core.view.forEach
import androidx.databinding.BindingAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.ServerType

/*@BindingAdapter("itemsPics")
fun RecyclerView.setPicsItems(waifus: List<WaifuPicItem>?) {
    if (waifus != null) {
        (adapter as? WaifuPicsAdapter)?.submitList(waifus)
    }
}*/

/*@BindingAdapter("itemsIm")
fun RecyclerView.setImItems(waifus: List<WaifuImItem>?) {
    if (waifus != null) {
        (adapter as? WaifuImAdapter)?.submitList(waifus)
    }
}*/

@BindingAdapter("onChooseTypeChanged")
fun ChipGroup.onChooseTypeChanged(listener: OnChooseTypeChanged?) {
    setOnCheckedStateChangeListener { group, checkedId ->
        group.forEach {
            val chip = it as Chip
            if (chip.id == checkedId.first()) {
                val type = when (chip.text) {
                    context.getString(R.string.server_normal) -> ServerType.NORMAL
                    context.getString(R.string.server_best) -> ServerType.NEKOS
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
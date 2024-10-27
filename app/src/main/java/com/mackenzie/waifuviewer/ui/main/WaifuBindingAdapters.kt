package com.mackenzie.waifuviewer.ui.main

import androidx.core.view.forEach
import androidx.databinding.BindingAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.ServerType

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
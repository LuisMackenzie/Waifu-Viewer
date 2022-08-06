package com.mackenzie.waifuviewer.ui.common

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("url")
fun ImageView.bindUrl(url: String?) {
    if (url != null) loadUrl(url)
}

@BindingAdapter("visible")
fun View.setVisible(visible: Boolean?) {
    visibility = if (visible == true) View.VISIBLE else View.GONE
}

/*
@BindingAdapter("idText")
fun TextView.textIm(id: Int?) {
    if (id != null) text = id.toString()
}

@BindingAdapter("idTextPic")
fun TextView.textPic(id: Int?) {
    if (id != null) text = id.toString()
}*/

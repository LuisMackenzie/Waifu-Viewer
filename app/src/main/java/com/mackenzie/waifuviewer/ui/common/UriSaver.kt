package com.mackenzie.waifuviewer.ui.common

import android.net.Uri
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.core.net.toUri

class UriSaver : Saver<MutableList<Uri>, List<String>> {
    override fun restore(value: List<String>): MutableList<Uri> = value.map {
        it.toUri()
    }.toMutableList()

    override fun SaverScope.save(value: MutableList<Uri>): List<String> = value.map { it.toString() }
}
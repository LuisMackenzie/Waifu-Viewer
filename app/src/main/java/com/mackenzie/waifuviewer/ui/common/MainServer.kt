package com.mackenzie.waifuviewer.ui.common

class MainServer() {
    private var _server = false
    val server: Boolean
        get() = _server

    fun setServer(value: Boolean) {
        _server = value
    }
}
package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.NotificationLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.TokenLocalDataSource
import javax.inject.Inject

class PushRepository@Inject constructor(
    private val tokenDataSource: TokenLocalDataSource,
    private val pushDataSource: NotificationLocalDataSource
) {




}
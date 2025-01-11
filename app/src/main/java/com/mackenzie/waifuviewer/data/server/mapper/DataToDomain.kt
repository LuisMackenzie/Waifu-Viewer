package com.mackenzie.waifuviewer.data.server.mapper

import android.os.Build
import com.mackenzie.waifuviewer.data.PushRepository
import com.mackenzie.waifuviewer.domain.FcmToken
import com.mackenzie.waifuviewer.ui.common.getCurrentLocalDateTime
import com.mackenzie.waifuviewer.ui.common.getDefaultTokenValidity


fun String.toDomainModel(): FcmToken = FcmToken(
    0,
    this,
    getCurrentLocalDateTime(),
    getDefaultTokenValidity(),
    Build.BRAND,
    Build.MODEL,
    Build.ID
)


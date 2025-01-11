package com.mackenzie.waifuviewer.data.server.mapper

import android.os.Build
import com.mackenzie.waifuviewer.domain.FcmToken
import com.mackenzie.waifuviewer.ui.common.getCurrentLocalDateTime


fun String.toDomainModel(): FcmToken = FcmToken(
    0,
    this,
    getCurrentLocalDateTime(),
    Build.BRAND,
    Build.MODEL
)


package com.mackenzie.waifuviewer.domain.video

import com.mackenzie.waifuviewer.domain.VideoItem.Type

data class ServersVideoHubItemResponse(
    val serverList: List<ServersItemResponse> = emptyList(),
    val url : String? = null,
    val errorMessage: String? = null,
    val message: String?
)

data class ServersItemResponse(
    val id: Int,
    val title: String,
    val thumb: String,
    val url: String,
    val type: Type,
    val description: String
)

package com.mackenzie.waifuviewer.domain

data class LoadingState(
    var loadMoreIm: Boolean = false,
    var loadMorePics: Boolean = false,
    var loadMoreBest: Boolean = false,
)

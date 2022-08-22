package usecases

import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.domain.WaifuPicItem

internal val sampleImWaifu = WaifuImItem(
    id = 0,
    dominantColor = "",
    file = "",
    height = "",
    width = "",
    imageId = 6969,
    isNsfw = false,
    url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
    isFavorite = true
)

internal val samplePicWaifu = WaifuPicItem(
    id = 0,
    url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
    isFavorite = true
)

internal val sampleFavWaifu = FavoriteItem(
    id = 0,
    title = "Waifu Test",
    url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
    isFavorite = true
)
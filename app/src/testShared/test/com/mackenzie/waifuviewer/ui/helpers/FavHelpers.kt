package com.mackenzie.waifuviewer.ui

import com.mackenzie.waifuviewer.data.FavoritesRepository
import com.mackenzie.waifuviewer.data.db.FavoriteDbItem
import com.mackenzie.waifuviewer.data.db.datasources.FavoriteDataSource
import com.mackenzie.waifuviewer.ui.fakes.FakeFavoriteDao

fun buildFavRepositoryWith(
    localData: List<FavoriteDbItem>
): FavoritesRepository {
    val favoriteDataSource = FavoriteDataSource(FakeFavoriteDao(localData))
    return FavoritesRepository(favoriteDataSource)
}

fun buildFavDatabaseWaifus(vararg id: Int) = id.map {
    FavoriteDbItem(
        id = it,
        url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
        title = "Overview $it",
        isFavorite = false
    )
}

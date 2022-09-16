package com.mackenzie.waifuviewer.data.db.datasources

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.*
import com.mackenzie.waifuviewer.data.db.WaifuImDao
import com.mackenzie.waifuviewer.data.db.WaifuImDbItem
import com.mackenzie.waifuviewer.domain.WaifuImItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.math.max

class LocalImPagingSource @Inject constructor(private val imDao: WaifuImDao) : PagingSource<Int, WaifuImItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, WaifuImItem> {
        val start = params.key ?: STARTING_KEY
        val range = start.until(start + params.loadSize)
        var waifus: List<WaifuImItem> = listOf()
        imDao.getAllIm().collectLatest { waifus = it.toDomainModel() }

        return LoadResult.Page(


            data =  waifus,
            /*data = range.map { number ->
                WaifuImItem(
                    // Generate consecutive increasing numbers as the article id
                    id = number,
                    dominantColor = "",
                    file = "",
                    height = "",
                    imageId = 6969,
                    isNsfw = false,
                    url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
                    width = "",
                    isFavorite = false
                )
            },*/

            // Make sure we don't try to load items behind the STARTING_KEY
            prevKey = when (start) {
                STARTING_KEY -> null
                else -> ensureValidKey(key = range.first - params.loadSize)
            },
            nextKey = range.last + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, WaifuImItem>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val waifuIm = state.closestItemToPosition(anchorPosition) ?: return null
        return ensureValidKey(key = waifuIm.id - (state.config.pageSize / 2))
    }

    /**
     * Makes sure the paging key is never less than [STARTING_KEY]
     */
    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)

}

private fun List<WaifuImDbItem>.toDomainModel(): List<WaifuImItem> = map { it.toDomainModel() }

private fun WaifuImDbItem.toDomainModel(): WaifuImItem =
    WaifuImItem(
        id,
        signature,
        extension,
        dominantColor,
        source,
        uploadedAt,
        isNsfw,
        width,
        height,
        imageId,
        url,
        previewUrl,
        isFavorite
    )

private const val STARTING_KEY = 0
private const val LOAD_DELAY_MILLIS = 3_000L

@RequiresApi(Build.VERSION_CODES.O)
private val firstArticleCreatedTime = LocalDateTime.now()
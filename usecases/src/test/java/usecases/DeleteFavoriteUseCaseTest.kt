package usecases

import com.mackenzie.waifuviewer.data.FavoritesRepository
import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.usecases.ClearWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.DeleteFavoriteUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DeleteFavoriteUseCaseTest {

    @Test
    fun `Invoke calls PICS repository`(): Unit = runBlocking {
        val waifu = sampleFavWaifu.copy(id = 8)
        val repo = mock<FavoritesRepository>()
        val deleteFavoriteUseCase = DeleteFavoriteUseCase(repo)

        deleteFavoriteUseCase(waifu)

        verify(repo).deleteFavorite(any())
    }

}
package usecases

import com.mackenzie.testshared.sampleFavWaifu
import com.mackenzie.waifuviewer.data.FavoritesRepository
import com.mackenzie.waifuviewer.usecases.SwitchFavoriteUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class SwitchFavoriteUseCaseTest {

    @Test
    fun `Invoke calls Favorite repository`(): Unit = runBlocking {
        val waifu = sampleFavWaifu.copy(id = 1)
        val repo = mock<FavoritesRepository>()
        val switchFavoriteUseCase = SwitchFavoriteUseCase(repo)

        switchFavoriteUseCase(waifu)

        verify(repo).switchFavorite(any())
    }


}
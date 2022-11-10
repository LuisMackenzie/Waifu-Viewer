package usecases.best

import com.mackenzie.testshared.sampleBestWaifu
import com.mackenzie.waifuviewer.data.WaifusBestRepository
import com.mackenzie.waifuviewer.usecases.best.SwitchBestFavoriteUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class SwitchBestFavoriteUseCaseTest {

    @Test
    fun `Invoke calls BEST repository`(): Unit = runBlocking {
        val waifu = sampleBestWaifu.copy(id = 1)
        val repo = mock<WaifusBestRepository>()
        val switchFavoriteUseCase = SwitchBestFavoriteUseCase(repo)

        switchFavoriteUseCase(waifu)

        verify(repo).switchFavorite(waifu)
    }
}
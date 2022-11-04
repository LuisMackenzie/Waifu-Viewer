package usecases

import com.mackenzie.testshared.sampleImWaifu
import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.usecases.im.SwitchImFavoriteUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class SwitchImFavoriteUseCaseTest {

    @Test
    fun `Invoke calls Im repository`(): Unit = runBlocking {
        val waifu = sampleImWaifu.copy(id = 1)
        val repo = mock<WaifusImRepository>()
        val switchImFavoriteUseCase = SwitchImFavoriteUseCase(repo)

        switchImFavoriteUseCase(waifu)

        verify(repo).switchImFavorite(waifu)
    }

}
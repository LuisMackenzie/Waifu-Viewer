package usecases

import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.usecases.SwitchImFavoriteUseCase
import com.mackenzie.waifuviewer.usecases.SwitchPicFavoriteUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class SwitchPicFavoriteUseCaseTest {

    @Test
    fun `Invoke calls Pics repository`(): Unit = runBlocking {
        val waifu = samplePicWaifu.copy(id = 1)
        val repo = mock<WaifusPicRepository>()
        val switchPicFavoriteUseCase = SwitchPicFavoriteUseCase(repo)

        switchPicFavoriteUseCase(waifu)

        verify(repo).switchPicsFavorite(waifu)
    }

}
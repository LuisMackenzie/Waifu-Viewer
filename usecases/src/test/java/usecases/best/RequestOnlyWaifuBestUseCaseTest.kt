package usecases.best

import com.mackenzie.waifuviewer.data.WaifusBestRepository
import com.mackenzie.waifuviewer.usecases.best.RequestOnlyWaifuBestUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class RequestOnlyWaifuBestUseCaseTest {

    @Test
    fun `Invoke calls BEST repository`(): Unit = runBlocking {
        val repo = mock<WaifusBestRepository>()
        val requestOnlyWaifuUseCase = RequestOnlyWaifuBestUseCase(repo)

        requestOnlyWaifuUseCase()

        verify(repo).requestOnlyWaifu()
    }

}
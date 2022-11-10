package usecases.best

import com.mackenzie.waifuviewer.data.WaifusBestRepository
import com.mackenzie.waifuviewer.usecases.best.RequestMoreWaifuBestUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class RequestMoreWaifuBestUseCaseTest {

    @Test
    fun `Invoke calls BEST repository`(): Unit = runBlocking {
        val repo = mock<WaifusBestRepository>()
        val requestMoreWaifuUseCase = RequestMoreWaifuBestUseCase(repo)

        requestMoreWaifuUseCase("waifu")

        verify(repo).requestNewWaifus(any())
    }

}
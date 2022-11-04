package usecases

import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.usecases.pics.RequestMoreWaifuPicUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class RequestMoreWaifuPicUseCaseTest {

    @Test
    fun `Invoke calls PICS repository`(): Unit = runBlocking {
        val repo = mock<WaifusPicRepository>()
        val requestMoreWaifuPicUseCase = RequestMoreWaifuPicUseCase(repo)

        requestMoreWaifuPicUseCase("sfw", "waifu")

        verify(repo).requestNewWaifusPics(any(), any())
    }

}
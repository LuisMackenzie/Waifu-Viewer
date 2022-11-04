package usecases

import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.usecases.pics.RequestWaifuPicUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class RequestWaifuPicUseCaseTest {

    @Test
    fun `Invoke calls PICS repository`(): Unit = runBlocking {
        val repo = mock<WaifusPicRepository>()
        val requestWaifuPicUseCase = RequestWaifuPicUseCase(repo)

        requestWaifuPicUseCase("sfw", "waifu")

        verify(repo).requestWaifusPics(any(), any())
    }

}
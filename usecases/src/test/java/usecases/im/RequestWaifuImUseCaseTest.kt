package usecases.im

import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.usecases.im.RequestWaifuImUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

class RequestWaifuImUseCaseTest {

    @Test
    fun `Invoke calls Im repository`(): Unit = runBlocking {
        val repo = mock<WaifusImRepository>()
        val requestWaifuImUseCase = RequestWaifuImUseCase(repo)

        requestWaifuImUseCase(false, "waifu", false, false)

        verify(repo).requestWaifusIm(any(),any(),any(),any())
    }

}
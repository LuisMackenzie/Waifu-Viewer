package usecases

import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.usecases.im.RequestMoreWaifuImUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class RequestMoreWaifuImUseCaseTest {

    @Test
    fun `Invoke calls IM repository`(): Unit = runBlocking {
        val repo = mock<WaifusImRepository>()
        val requestMoreWaifuImUseCase = RequestMoreWaifuImUseCase(repo)

        requestMoreWaifuImUseCase(false, "waifu", false, false)

        verify(repo).requestNewWaifusIm(any(),any(),any(),any())
    }

}
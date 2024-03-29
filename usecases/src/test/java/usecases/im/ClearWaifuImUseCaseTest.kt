package usecases.im

import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.usecases.im.ClearWaifuImUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ClearWaifuImUseCaseTest {

    @Test
    fun `Invoke calls IM repository`(): Unit = runBlocking {
        val repo = mock<WaifusImRepository>()
        val clearWaifuImUseCase = ClearWaifuImUseCase(repo)

        clearWaifuImUseCase()

        verify(repo).requestClearWaifusIm()
    }

}
package usecases.best

import com.mackenzie.waifuviewer.data.WaifusBestRepository
import com.mackenzie.waifuviewer.usecases.best.ClearWaifuBestUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ClearWaifuBestUseCaseTest {

    @Test
    fun `Invoke calls Best repository`(): Unit = runBlocking {
        val repo = mock<WaifusBestRepository>()
        val clearWaifuUseCase = ClearWaifuBestUseCase(repo)

        clearWaifuUseCase()

        verify(repo).requestClearWaifusBest()
    }

}
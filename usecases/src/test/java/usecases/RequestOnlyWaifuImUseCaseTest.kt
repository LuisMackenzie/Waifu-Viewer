package usecases

import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.usecases.RequestOnlyWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.RequestWaifuImUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class RequestOnlyWaifuImUseCaseTest {

    @Test
    fun `Invoke calls Im repository`(): Unit = runBlocking {
        val repo = mock<WaifusImRepository>()
        val requestOnlyWaifuImUseCase = RequestOnlyWaifuImUseCase(repo)

        requestOnlyWaifuImUseCase()

        verify(repo).requestOnlyWaifuIm()
    }

}
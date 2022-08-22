package usecases

import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.usecases.ClearWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.ClearWaifuPicUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ClearWaifuPicUseCaseTest {

    @Test
    fun `Invoke calls PICS repository`(): Unit = runBlocking {
        val repo = mock<WaifusPicRepository>()
        val clearWaifuPicUseCase = ClearWaifuPicUseCase(repo)

        clearWaifuPicUseCase()

        verify(repo).requestClearWaifusPic()
    }

}
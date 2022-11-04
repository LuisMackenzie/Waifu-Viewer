package usecases

import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.usecases.pics.RequestOnlyWaifuPicUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class RequestOnlyWaifuPicUseCaseTest {

    @Test
    fun `Invoke calls PICS repository`(): Unit = runBlocking {
        val repo = mock<WaifusPicRepository>()
        val requestOnlyWaifuPicUseCase = RequestOnlyWaifuPicUseCase(repo)

        requestOnlyWaifuPicUseCase()

        verify(repo).requestOnlyWaifuPic()
    }

}
package usecases.pics

import com.mackenzie.testshared.samplePicWaifu
import com.mackenzie.waifuviewer.usecases.pics.FindWaifuPicUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class FindWaifuPicUseCaseTest {

    @Test
    fun `Invoke calls PICS repository`(): Unit = runBlocking {
        val waifu = flowOf(samplePicWaifu.copy(id = 1))
        val findWaifuPicUseCase = FindWaifuPicUseCase(mock() {
            on { findPicsById(1) } doReturn (waifu)
        })

        val result = findWaifuPicUseCase(1)

        assertEquals(waifu, result)
    }

}
package usecases.best

import com.mackenzie.testshared.sampleBestWaifu
import com.mackenzie.waifuviewer.usecases.best.FindWaifuBestUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class FindWaifuBestUseCaseTest {

    @Test
    fun `Invoke calls BEST repository`(): Unit = runBlocking {
        val waifu = flowOf(sampleBestWaifu.copy(id = 1))
        val findWaifuUseCase = FindWaifuBestUseCase(mock() {
            on { findById(1) } doReturn (waifu)
        })

        val result = findWaifuUseCase(1)

        Assert.assertEquals(waifu, result)
    }

}
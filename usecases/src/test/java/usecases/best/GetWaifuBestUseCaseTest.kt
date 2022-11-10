package usecases.best

import com.mackenzie.testshared.sampleBestWaifu
import com.mackenzie.waifuviewer.usecases.best.GetWaifuBestUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class GetWaifuBestUseCaseTest {

    @Test
    fun `Invoke calls waifu BEST repository`(): Unit = runBlocking {
        val waifus = flowOf(listOf(sampleBestWaifu.copy(id = 1)))
        val getWaifuUseCase = GetWaifuBestUseCase(mock() {
            on { savedWaifus } doReturn waifus
        })

        val result = getWaifuUseCase()

        Assert.assertEquals(waifus, result)
    }

}
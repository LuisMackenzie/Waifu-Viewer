package usecases.im

import com.mackenzie.testshared.sampleImWaifu
import com.mackenzie.waifuviewer.usecases.im.GetWaifuImUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class GetWaifuImUseCaseTest {

    @Test
    fun `Invoke calls waifu IM repository`(): Unit = runBlocking {
        val waifus = flowOf(listOf(sampleImWaifu.copy(id = 1)))
        val getWaifuImUseCase = GetWaifuImUseCase(mock() {
            on { savedWaifusIm } doReturn waifus
        })

        val result = getWaifuImUseCase()

        assertEquals(waifus, result)
    }
}
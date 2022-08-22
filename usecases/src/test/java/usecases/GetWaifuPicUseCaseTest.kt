package usecases

import com.mackenzie.waifuviewer.usecases.GetWaifuPicUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class GetWaifuPicUseCaseTest {

    @Test
    fun `Invoke calls waifu PICS repository`(): Unit = runBlocking {
        val waifus = flowOf(listOf(samplePicWaifu.copy(id = 1)))
        val getWaifuPicUseCase = GetWaifuPicUseCase(mock() {
            on { savedWaifusPic } doReturn waifus
        })

        val result = getWaifuPicUseCase()

        assertEquals(waifus, result)
    }

}
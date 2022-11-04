package usecases

import com.mackenzie.waifuviewer.usecases.im.FindWaifuImUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.junit.Test

class FindWaifuImUseCaseTest {

    @Test
    fun `Invoke calls IM repository`(): Unit = runBlocking {
        val waifu = flowOf(sampleImWaifu.copy(id = 1))
        val findWaifuImUseCase = FindWaifuImUseCase(mock() {
            on { findImById(1) } doReturn (waifu)
        })

        val result = findWaifuImUseCase(1)

        assertEquals(waifu, result)
    }

}
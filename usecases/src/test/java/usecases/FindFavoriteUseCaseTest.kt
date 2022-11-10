package usecases

import com.mackenzie.testshared.sampleFavWaifu
import com.mackenzie.waifuviewer.usecases.FindFavoriteUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class FindFavoriteUseCaseTest {

    @Test
    fun `Invoke calls Favorite repository`(): Unit = runBlocking {
        val waifu = flowOf(sampleFavWaifu.copy(id = 1))
        val findFavoriteUseCase = FindFavoriteUseCase(mock() {
            on { findFavoriteById(1) } doReturn (waifu)
        })

        val result = findFavoriteUseCase(1)

        assertEquals(waifu, result)
    }

}
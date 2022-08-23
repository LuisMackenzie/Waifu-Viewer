package usecases

import com.mackenzie.testshared.sampleFavWaifu
import com.mackenzie.waifuviewer.usecases.GetFavoritesUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class GetFavoritesUseCaseTest {

    @Test
    fun `Invoke calls waifu Favorite repository`(): Unit = runBlocking {
        val waifus = flowOf(listOf(sampleFavWaifu.copy(id = 1)))
        val getWaifuFavUseCase = GetFavoritesUseCase(mock() {
            on { savedFavorites } doReturn waifus
        })

        val result = getWaifuFavUseCase()

        assertEquals(waifus, result)
    }

}
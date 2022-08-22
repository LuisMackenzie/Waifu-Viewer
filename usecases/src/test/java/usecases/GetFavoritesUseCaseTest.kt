package usecases

import com.mackenzie.waifuviewer.usecases.GetFavoritesUseCase
import com.mackenzie.waifuviewer.usecases.GetWaifuImUseCase
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
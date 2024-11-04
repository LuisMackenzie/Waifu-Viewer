package com.mackenzie.waifuviewer.ui.detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.DownloadModel
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.*
import com.mackenzie.waifuviewer.domain.getTypes
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.common.SaveImage
import com.mackenzie.waifuviewer.ui.common.showToast
import com.mackenzie.waifuviewer.ui.detail.ui.DetailBestScreenContent
import com.mackenzie.waifuviewer.ui.detail.ui.DetailFavsScreenContent
import com.mackenzie.waifuviewer.ui.detail.ui.DetailImScreenContent
import com.mackenzie.waifuviewer.ui.detail.ui.DetailPicsScreenContent
import com.mackenzie.waifuviewer.ui.main.MainState
import com.mackenzie.waifuviewer.ui.main.buildMainState
import com.mackenzie.waifuviewer.ui.main.ui.MainTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val picsViewModel: DetailPicsViewModel by viewModels()
    private val imViewModel: DetailImViewModel by viewModels()
    private val bestViewModel: DetailBestViewModel by viewModels()
    private val favsViewModel: DetailFavsViewModel by viewModels()
    private lateinit var mainState: MainState
    private lateinit var download: DownloadModel
    private var isWritePermissionGranted: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mainState = buildMainState()
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val serverMode = sharedPref.getString(Constants.SERVER_MODE, "") ?: ""

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MainTheme {
                    LaunchDefaultScreen(serverMode.getTypes())
                }
            }
        }
    }

    @Composable
    private fun LaunchDefaultScreen(mode: ServerType) {
        when (mode) {
            NORMAL -> DetailImScreen()
            ENHANCED -> DetailPicsScreen()
            NEKOS -> DetailNekosScreen()
            FAVORITE -> DetailFavsScreen()
            WAIFUGPT -> {}
        }
    }



    @Composable
    private fun DetailImScreen() {
        DetailImScreenContent(
            state = imViewModel.state.collectAsStateWithLifecycle().value,
            prepareDownload = { title, link, imageExt -> prepareDownload(title, link, imageExt) },
            onFavoriteClicked = { imViewModel.onFavoriteClicked() },
            onDownloadClick = { onDownloadClick() },
            onSearchClick = { imViewModel.onSearchClicked(it) }
        )
    }

    @Composable
    private fun DetailPicsScreen() {
        DetailPicsScreenContent(
            state = picsViewModel.state.collectAsStateWithLifecycle().value,
            prepareDownload = { title, link, imageExt -> prepareDownload(title, link, imageExt) },
            onFavoriteClicked = { picsViewModel.onFavoriteClicked() },
            onDownloadClick = { onDownloadClick() },
            onSearchClick = { picsViewModel.onSearchClicked(it) }
        )
    }

    @Composable
    private fun DetailNekosScreen() {
        DetailBestScreenContent(
            state = bestViewModel.state.collectAsStateWithLifecycle().value,
            prepareDownload = { title, link, imageExt -> prepareDownload(title, link, imageExt) },
            onFavoriteClicked = { bestViewModel.onFavoriteClicked() },
            onDownloadClick = { onDownloadClick() },
            // El servicio no permite enviar imagenes de este server
            onSearchClick = { notReady() } //  { bestViewModel.onSearchClicked(it) }
        )
    }

    @Composable
    private fun DetailFavsScreen() {
        DetailFavsScreenContent(
            state = favsViewModel.state.collectAsStateWithLifecycle().value,
            prepareDownload = { title, link, imageExt -> prepareDownload(title, link, imageExt) },
            onFavoriteClicked = { favsViewModel.onFavoriteClicked() },
            onDownloadClick = { onDownloadClick() },
            onSearchClick = { favsViewModel.onSearchClicked(it) }
        )
    }

    private fun notReady() {
        getString(R.string.function_not_implemented).showToast(requireContext())
    }

    private fun prepareDownload(title: String, link: String, imageExt: String) {
        download = DownloadModel(title, link, imageExt)
    }

    private fun onDownloadClick() {
        if (isWritePermissionGranted != true) {
            RequestPermision()
        }
        requestDownload()
    }

    private fun RequestPermision() {
        viewLifecycleOwner.lifecycleScope.launch {
            mainState.requestPermissionLauncher { isWritePermissionGranted = it }
        }
    }

    private fun requestDownload() {
        downloadImage(download.title, download.link, download.imageExt)
    }

    private fun downloadImage(title: String, link: String, fileType: String) {
        val type: String = selectMimeType(fileType)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val url = URL(link)
                val image: Bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                SaveImage().saveImageToStorage(
                    requireContext(),
                    image,
                    title,
                    type,
                )
            } catch (e: IOException) {
                Log.d(Constants.CATEGORY_TAG_DETAIL, "error: ${e.localizedMessage}")
            }
        }
    }

    private fun selectMimeType(fileType: String): String {
        when (fileType) {
            "jpg" -> return "image/jpeg"
            "jpeg" -> return "image/jpeg"
            "png" -> return "image/png"
            "gif" -> return "image/gif"
            else -> {
                return "image/jpeg"
            }
        }
    }
}

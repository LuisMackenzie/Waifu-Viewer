package com.mackenzie.waifuviewer.ui.detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil3.compose.AsyncImage
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.FragmentDetailBinding
import com.mackenzie.waifuviewer.domain.DownloadModel
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.*
import com.mackenzie.waifuviewer.domain.getTypes
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.common.SaveImage
import com.mackenzie.waifuviewer.ui.common.loadUrl
import com.mackenzie.waifuviewer.ui.common.visible
import com.mackenzie.waifuviewer.ui.main.MainState
import com.mackenzie.waifuviewer.ui.main.buildMainState
import com.mackenzie.waifuviewer.ui.theme.WaifuViewerTheme
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
    // private var serverMode: ServerType = NORMAL
    private var isWritePermissionGranted: Boolean = false

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = buildMainState()
        val binding = FragmentDetailBinding.bind(view)
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        serverMode = sharedPref.getString(Constants.SERVER_MODE, "") ?: ""
        binding.setUpElements()
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = buildMainState()
        /*serverMode = (requireActivity()
            .getPreferences(Context.MODE_PRIVATE)
            .getString(Constants.SERVER_MODE, "") ?: "normal"
                ).getTypes()*/
        // setUpElements(serverMode)
    }

    @Composable
    fun DetailScreenContent(
        // state: DetailImViewModel.UiState? = null,
        onFavoriteClicked: () -> Unit = {},
        onDownloadClicked: () -> Unit = {}
    ) {

        val state = imViewModel.state.collectAsState().value

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            state.waifuIm?.let { waifu ->
                Log.e("DetailFragment", "WaifuIm: $waifu")
                Text(text = waifu.imageId.toString(), style = MaterialTheme.typography.bodyMedium)
                AsyncImage(
                    // painter = rememberImagePainter(waifu.url),
                    model = waifu.url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Inside
                )
                IconButton(onClick = onFavoriteClicked) {
                    Icon(
                        painter = painterResource(
                            id = if (waifu.isFavorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
                        ),
                        contentDescription = null
                    )
                }
                Button(onClick = onDownloadClicked) {
                    Text(text = "Download")
                }
            }
            state.error?.let {

                Text(text = it.toString(), color = MaterialTheme.colorScheme.error)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Instead of inflating an XML layout, return a ComposeView
        return ComposeView(requireContext()).apply {
            setContent {
                WaifuViewerTheme {
                    DetailScreenContent()
                }
            }
        }
    }

    private fun getServerMode(mode: ServerType) {
        when (mode) {
            NORMAL -> {
                launchImCollect()
                // fab.setOnClickListener { imViewModel.onFavoriteClicked() }
            }
            ENHANCED -> {
                // launchPicsCollect()
                // fab.setOnClickListener { picsViewModel.onFavoriteClicked() }
            }
            NEKOS -> {
                // launchBestCollect()
                // fab.setOnClickListener { bestViewModel.onFavoriteClicked() }
            }
            FAVORITE -> {
                // launchFavoriteCollect()
                // fab.setOnClickListener { favsViewModel.onFavoriteClicked() }
            }
            else -> {
                // fab.visible = false
            }
        }
        /*fabDownload.setOnClickListener {
            if (isWritePermissionGranted != true) {
                RequestPermision()
            }
            requestDownload()
        }*/
    }

    private fun withImStateUpdateUI(state: DetailImViewModel.UiState) {
        // pbLoading.visibility = View.GONE

        /*setContent {
            DetailScreenContent(
                state = state,
                onFavoriteClicked = { imViewModel.onFavoriteClicked() },
                onDownloadClicked = { requestDownload() }
            )
        }*/

        state.waifuIm?.let {
            // tvDetail.text = it.imageId.toString()
            // ivDetail.loadUrl(it.url)
            Log.e("DetailFragment", "WaifuImItemURL: ${it.url}")
            if (it.isFavorite) {
                // fab.setImageResource(R.drawable.ic_favorite_on)
            } else {
                // fab.setImageResource(R.drawable.ic_favorite_off)
            }
            prepareDownload(it.imageId.toString(), it.url, it.url.substringAfterLast('.'))
        }

        state.error?.let {
            // tvDetail.text = getString(R.string.waifu_error)
            // ivDetail.setImageResource(R.drawable.ic_offline_background)
        }
    }

    // crear logica para abstraer las 4 funciones iguales
    /*private fun FragmentDetailBinding.launchPicsCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                picsViewModel.state.collect {
                    withPicsUpdateUI(it)
                }
            }
        }
    }*/

    /*private fun FragmentDetailBinding.launchImCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                imViewModel.state.collect {
                    withImUpdateUI(it)
                }
            }
        }
    }*/

    private fun launchImCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                imViewModel.state.collect {
                    withImStateUpdateUI(it)
                }
            }
        }
    }

    /*private fun FragmentDetailBinding.launchBestCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bestViewModel.state.collect {
                    withBestUpdateUI(it)
                }
            }
        }
    }*/

    /*private fun FragmentDetailBinding.launchFavoriteCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favsViewModel.state.collect {
                    withFavsUpdateUI(it)
                }
            }
        }
    }*/

    /*private fun FragmentDetailBinding.withBestUpdateUI(state: DetailBestViewModel.UiState) {
        pbLoading.visibility = View.GONE
        state.waifu?.let {
            val title = it.url.substringAfterLast('/').substringBeforeLast('.')
            if (it.artistName.isEmpty()) {
                tvDetail.text = it.animeName
            } else {
                tvDetail.text = it.artistName
            }
            ivDetail.loadUrl(it.url)
            if (it.isFavorite) {
                fab.setImageResource(R.drawable.ic_favorite_on)
            } else {
                fab.setImageResource(R.drawable.ic_favorite_off)
            }
            prepareDownload(title, it.url, it.url.substringAfterLast('.'))
        }

        state.error?.let {
            tvDetail.text = getString(R.string.waifu_error)
            ivDetail.setImageResource(R.drawable.ic_offline_background)
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }*/

    /*private fun FragmentDetailBinding.withFavsUpdateUI(state: DetailFavsViewModel.UiState) {
        pbLoading.visibility = View.GONE
        state.waifu?.let {
            tvDetail.text = it.url.substringAfterLast('/').substringBeforeLast('.')
            ivDetail.loadUrl(it.url)
            if (it.isFavorite) {
                fab.setImageResource(R.drawable.ic_favorite_on)
            } else {
                fab.setImageResource(R.drawable.ic_favorite_off)
            }
            prepareDownload(it.title, it.url, it.url.substringAfterLast('.'))
        }

        state.error?.let {
            tvDetail.text = getString(R.string.waifu_error)
            ivDetail.setImageResource(R.drawable.ic_offline_background)
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }*/

    /*private fun FragmentDetailBinding.withPicsUpdateUI(state: DetailPicsViewModel.UiState) {
        pbLoading.visibility = View.GONE
        state.waifuPic?.let {
            val title = it.url.substringAfterLast('/').substringBeforeLast('.')
            tvDetail.text = title
            ivDetail.loadUrl(it.url)
            if (it.isFavorite) {
                fab.setImageResource(R.drawable.ic_favorite_on)
            } else {
                fab.setImageResource(R.drawable.ic_favorite_off)
            }
            prepareDownload(title, it.url, it.url.substringAfterLast('.'))
        }

        state.error?.let {
            tvDetail.text = getString(R.string.waifu_error)
            ivDetail.setImageResource(R.drawable.ic_offline_background)
        }
    }*/

    /*private fun FragmentDetailBinding.withImUpdateUI(state: DetailImViewModel.UiState) {
        pbLoading.visibility = View.GONE
        state.waifuIm?.let {
            tvDetail.text = it.imageId.toString()
            ivDetail.loadUrl(it.url)
            if (it.isFavorite) {
                fab.setImageResource(R.drawable.ic_favorite_on)
            } else {
                fab.setImageResource(R.drawable.ic_favorite_off)
            }
            prepareDownload(it.imageId.toString(), it.url, it.url.substringAfterLast('.'))
        }

        state.error?.let {
            tvDetail.text = getString(R.string.waifu_error)
            ivDetail.setImageResource(R.drawable.ic_offline_background)
        }
    }*/

    private fun prepareDownload(title: String, link: String, imageExt: String) {
        download = DownloadModel(title, link, imageExt)
    }

    private fun setUpElements(mode: ServerType) {
        when (mode) {
            NORMAL -> {
                launchImCollect()
                // fab.setOnClickListener { imViewModel.onFavoriteClicked() }
            }
            ENHANCED -> {
                // launchPicsCollect()
                // fab.setOnClickListener { picsViewModel.onFavoriteClicked() }
            }
            NEKOS -> {
                // launchBestCollect()
                // fab.setOnClickListener { bestViewModel.onFavoriteClicked() }
            }
            FAVORITE -> {
                // launchFavoriteCollect()
                // fab.setOnClickListener { favsViewModel.onFavoriteClicked() }
            }
            else -> {
                // fab.visible = false
            }
        }
        /*fabDownload.setOnClickListener {
            if (isWritePermissionGranted != true) {
                RequestPermision()
            }
            requestDownload()
        }*/
    }

    /*private fun FragmentDetailBinding.setUpElements() {
        when (serverMode) {
            getString(R.string.server_normal_string) -> {
                launchImCollect()
                fab.setOnClickListener { imViewModel.onFavoriteClicked() }
            }
            getString(R.string.server_enhanced_string) -> {
                launchPicsCollect()
                fab.setOnClickListener { picsViewModel.onFavoriteClicked() }
            }
            getString(R.string.server_nekos_string) -> {
                launchBestCollect()
                fab.setOnClickListener { bestViewModel.onFavoriteClicked() }
            }
            getString(R.string.server_favorite_string) -> {
                launchFavoriteCollect()
                fab.setOnClickListener { favsViewModel.onFavoriteClicked() }
            }
            else -> {
                fab.visible = false
            }
        }
        fabDownload.setOnClickListener {
            if (isWritePermissionGranted != true) {
                RequestPermision()
            }
            requestDownload()
        }
    }*/

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

                /*if (isGif(fileType)) {
                    var imageGif: ViewTarget<ImageView, GifDrawable>
                    Glide.with(requireContext()).asGif().load(link).into(imageGif)

                }*/
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

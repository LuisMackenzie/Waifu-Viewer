package com.mackenzie.waifuviewer.ui.detail

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.DownloadModel
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.*
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.common.SaveImage
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

    @Preview(showBackground = true, widthDp = 400, heightDp = 400)
    @Composable
    fun DetailScreenContent() {

        val state = imViewModel.state.collectAsState().value

        Box(modifier = Modifier
            .fillMaxSize()
        ) {
            state.waifuIm?.let { waifu ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(waifu.url)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_baseline_download),
                    // placeholder = lottiePlaceholder(),
                    // onLoading = { LoadingAnimation(modifier = Modifier.fillMaxSize()) },
                    error = painterResource(R.drawable.ic_failed),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp),
                    onClick = {imViewModel.onFavoriteClicked()}
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (waifu.isFavorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
                        ),
                        contentDescription = null
                    )
                }
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp),
                    text = waifu.imageId.toString(),
                    fontSize = 25.sp,
                    style = MaterialTheme.typography.bodyMedium
                )

                FloatingActionButton(
                    onClick = { onDownloadClick() },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_download),
                        contentDescription = null
                    )
                }
                prepareDownload(waifu.imageId.toString(), waifu.url, waifu.url.substringAfterLast('.'))
            }
            state.error?.let {

                LoadingAnimationError(modifier = Modifier.fillMaxSize())
                Text(
                    text = it.toString(),
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp)
                )
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

    @Composable
    fun LoadingAnimation(modifier: Modifier = Modifier) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))
        LottieAnimation(
            composition = composition,
            modifier = modifier
        )
    }

    @Composable
    fun lottiePlaceholder(): Painter {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))
        return rememberAsyncImagePainter(composition)
    }

    @Composable
    fun LoadingAnimationError(modifier: Modifier = Modifier) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_data))
        LottieAnimation(
            composition = composition,
            modifier = modifier
        )
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

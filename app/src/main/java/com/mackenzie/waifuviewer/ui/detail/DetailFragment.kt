package com.mackenzie.waifuviewer.ui.detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.FragmentDetailBinding
import com.mackenzie.waifuviewer.domain.DownloadModel
import com.mackenzie.waifuviewer.ui.common.Constants
import com.mackenzie.waifuviewer.ui.common.SaveImage
import com.mackenzie.waifuviewer.ui.common.loadUrl
import com.mackenzie.waifuviewer.ui.common.visible
import com.mackenzie.waifuviewer.ui.main.MainState
import com.mackenzie.waifuviewer.ui.main.buildMainState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL

@AndroidEntryPoint
class DetailFragment: Fragment(R.layout.fragment_detail) {

    private val picsViewModel: DetailPicsViewModel by viewModels()
    private val imViewModel: DetailImViewModel by viewModels()
    private val favsViewModel: DetailFavsViewModel by viewModels()
    private lateinit var mainState: MainState
    private lateinit var download: DownloadModel
    private var serverMode: String = ""
    private var isWritePermissionGranted: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = buildMainState()
        val binding = FragmentDetailBinding.bind(view)
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        serverMode = sharedPref.getString(Constants.SERVER_MODE, "") ?: ""
        binding.setUpElements()
    }

    private fun FragmentDetailBinding.launchPicsCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                picsViewModel.state.collect {
                    withPicsUpdateUI(it)
                }
            }
        }
    }

    private fun FragmentDetailBinding.launchImCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                imViewModel.state.collect {
                    withImUpdateUI(it)
                }
            }
        }
    }

    private fun FragmentDetailBinding.launchFavoriteCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favsViewModel.state.collect {
                    withFavsUpdateUI(it)
                }
            }
        }
    }

    private fun FragmentDetailBinding.withFavsUpdateUI(state: DetailFavsViewModel.UiState) {

        pbLoading.visibility = View.GONE
        state.waifu?.let {
            tvDetail.text = it.url.substringAfterLast('/').substringBeforeLast('.')
            ivDetail.loadUrl(it.url)
            if (it.isFavorite) {
                fabFavorites.setImageResource(R.drawable.ic_favorite_on)
            } else {
                fabFavorites.setImageResource(R.drawable.ic_favorite_off)
            }
            prepareDownload(it.title, it.url, it.url.substringAfterLast('.'))
        }

        state.error?.let {
            tvDetail.text = getString(R.string.waifu_error)
            ivDetail.setImageResource(R.drawable.ic_offline_background)
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun FragmentDetailBinding.withPicsUpdateUI(state: DetailPicsViewModel.UiState) {

        pbLoading.visibility = View.GONE
        state.waifuPic?.let {
            val title = it.url.substringAfterLast('/').substringBeforeLast('.')
            tvDetail.text = title
            ivDetail.loadUrl(it.url)
            if (it.isFavorite) {
                fabPics.setImageResource(R.drawable.ic_favorite_on)
            } else {
                fabPics.setImageResource(R.drawable.ic_favorite_off)
            }
            prepareDownload(title, it.url, it.url.substringAfterLast('.'))
        }

        state.error?.let {
            tvDetail.text = getString(R.string.waifu_error)
            ivDetail.setImageResource(R.drawable.ic_offline_background)

        }
    }

    private fun FragmentDetailBinding.withImUpdateUI(state: DetailImViewModel.UiState) {

        pbLoading.visibility = View.GONE
        state.waifuIm?.let {
            tvDetail.text = it.imageId.toString()
            ivDetail.loadUrl(it.url)
            if (it.isFavorite) {
                fabIm.setImageResource(R.drawable.ic_favorite_on)
            } else {
                fabIm.setImageResource(R.drawable.ic_favorite_off)
            }
            prepareDownload(it.imageId.toString(), it.url, it.url.substringAfterLast('.'))
        }

        state.error?.let {
            tvDetail.text = getString(R.string.waifu_error)
            ivDetail.setImageResource(R.drawable.ic_offline_background)
        }
    }

    private fun prepareDownload(title: String, link: String, imageExt: String) {
        download = DownloadModel(title, link, imageExt)
    }

    private fun FragmentDetailBinding.setUpElements() {
        when (serverMode) {
            getString(R.string.server_normal_string) -> {
                launchImCollect()
                fabIm.visible = true
                fabPics.visible = false
                fabFavorites.visible = false
            }
            getString(R.string.server_enhanced_string) -> {
                launchPicsCollect()
                fabPics.visible = true
                fabIm.visible = false
                fabFavorites.visible = false
            }
            getString(R.string.server_favorite_string) -> {
                launchFavoriteCollect()
                fabFavorites.visible = true
                fabPics.visible = false
                fabIm.visible = false
            }
            else -> {
                fabFavorites.visible = false
                fabPics.visible = false
                fabIm.visible = false
            }
        }

        fabIm.setOnClickListener { imViewModel.onFavoriteClicked() }
        fabPics.setOnClickListener { picsViewModel.onFavoriteClicked() }
        fabFavorites.setOnClickListener { favsViewModel.onFavoriteClicked() }
        fabDownload.setOnClickListener {
            if (isWritePermissionGranted != true ) {
                RequestPermision()
            }
            requestDownload()
        }
    }

    private fun RequestPermision() {
        viewLifecycleOwner.lifecycleScope.launch {
            mainState.requestPermissionLauncher {isWritePermissionGranted = it}
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
                    type
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
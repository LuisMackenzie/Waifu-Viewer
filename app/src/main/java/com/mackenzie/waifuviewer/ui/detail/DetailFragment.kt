package com.mackenzie.waifuviewer.ui.detail

import android.content.ContentValues.TAG
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
import androidx.navigation.fragment.navArgs
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.models.datasource.WaifusRepository
import com.mackenzie.waifuviewer.databinding.FragmentDetailBinding
import com.mackenzie.waifuviewer.models.db.WaifuImItem
import com.mackenzie.waifuviewer.models.db.WaifuPicItem
import com.mackenzie.waifuviewer.ui.common.app
import com.mackenzie.waifuviewer.ui.common.loadUrl
import com.mackenzie.waifuviewer.ui.common.visible
import com.mackenzie.waifuviewer.ui.main.MainState
import com.mackenzie.waifuviewer.ui.main.WaifuFragment.Companion.IS_SERVER_SELECTED
import com.mackenzie.waifuviewer.ui.main.buildMainState
import com.mackenzie.waifuviewer.utils.SaveImage
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL


class DetailFragment: Fragment(R.layout.fragment_detail) {

    private val safeArgs: DetailFragmentArgs by navArgs()

    private val picsViewModel: DetailPicsViewModel by viewModels {
        DetailPicsViewModelFactory(safeArgs.waifuId, WaifusRepository(requireActivity().app)) }
    private val imViewModel: DetailImViewModel by viewModels {
        DetailImViewModelFactory(safeArgs.waifuId, WaifusRepository(requireActivity().app)) }
    private lateinit var mainState: MainState
    private var mainServer: Boolean = false
    private var title: String? = null
    private var link: String? = null
    private var imageExt:String? = null
    private var isWritePermissionGranted: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = buildMainState()
        val binding = FragmentDetailBinding.bind(view)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        mainServer = sharedPref.getBoolean(IS_SERVER_SELECTED, false)
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

    private fun FragmentDetailBinding.withPicsUpdateUI(state: DetailPicsViewModel.UiState) {

        pbLoading.visibility = View.GONE
        state.waifuPic?.let {
            tvDetail.text = ""
            ivDetail.loadUrl(it.url)
            prepareDownloadPic(it)
        }
            /*state.idPic?.let {
                // tvDetail.text = it.imageId.toString()
                tvDetail.text = ""
                Toast.makeText(context, "Aqui esta el flujo PICS", Toast.LENGTH_SHORT).show()
            }*/
    }

    private fun FragmentDetailBinding.withImUpdateUI(state: DetailImViewModel.UiState) {

        pbLoading.visibility = View.GONE
        state.waifuIm?.let {
            tvDetail.text = it.imageId.toString()
            ivDetail.loadUrl(it.url)
            prepareDownloadIm(it)
        }
        /*state.idIm?.let {
            // tvDetail.text = it.imageId.toString()
            tvDetail.text = ""
            Toast.makeText(context, "Aqui esta el flujo IM", Toast.LENGTH_SHORT).show()
        }*/
    }

    private fun prepareDownloadPic(waifuPic: WaifuPicItem) {
        title = waifuPic.url.substringAfterLast('/')
        link = waifuPic.url
        imageExt = waifuPic.url.substringAfterLast('.')
    }

    private fun prepareDownloadIm(waifuIm: WaifuImItem) {
        title = waifuIm.file
        link = waifuIm.url
        imageExt = waifuIm.url.substringAfterLast('.')
    }

    private fun FragmentDetailBinding.setUpElements() {
        if (mainServer) {
            launchPicsCollect()
            favPics.visible = true
            favIm.visible = false
        } else {
            launchImCollect()
            favPics.visible = false
            favIm.visible = true
        }

        favIm.setOnClickListener { imViewModel.onFavoriteClicked() }
        favPics.setOnClickListener { picsViewModel.onFavoriteClicked() }
        fab.setOnClickListener {
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
        downloadImage(title!!, link!!, imageExt!!)
    }

    private fun downloadImage(title: String, link: String, fileType: String) {

        val type: String = selectMimeType(fileType)
        lifecycleScope.launch(IO) {
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
                Log.d(TAG, "error: ${e.localizedMessage}")
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

    fun setWaifuPics(waifu: WaifuPicItem) {

    }

    fun setWaifuIm(waifu: WaifuImItem) {

    }

    private fun isGif(imagen: String): Boolean = imagen.endsWith("gif")


}

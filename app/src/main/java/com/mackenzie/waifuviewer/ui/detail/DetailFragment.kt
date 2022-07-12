package com.mackenzie.waifuviewer.ui.detail

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.ViewTarget
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.databinding.FragmentDetailBinding
import com.mackenzie.waifuviewer.models.PermissionChecker
import com.mackenzie.waifuviewer.models.Waifu
import com.mackenzie.waifuviewer.ui.common.PermissionRequester
import com.mackenzie.waifuviewer.ui.common.loadUrl
import com.mackenzie.waifuviewer.ui.main.MainState
import com.mackenzie.waifuviewer.ui.main.WaifuFragment
import com.mackenzie.waifuviewer.ui.main.buildMainState
import com.mackenzie.waifuviewer.utils.SaveImage
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL


class DetailFragment: Fragment(R.layout.fragment_detail) {

    private val safeArgs: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(requireNotNull(safeArgs.waifuUrl)) }
    private lateinit var mainState: MainState
    private var title: String? = null
    private var link: String? = null
    private var imageExt:String? = null
    private var isWritePermissionGranted: Boolean = false
    // private val storagePermissionRequester = PermissionRequester(this , Manifest.permission.WRITE_EXTERNAL_STORAGE)
    /*private val requestPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        doRequestPermission(isGranted)
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = buildMainState()
        val binding = FragmentDetailBinding.bind(view)
        binding.setUpElements()

        // viewModel.state.observe(this, ::updateUI)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    binding.updateUI(it)
                }
            }
        }

    }



    private fun FragmentDetailBinding.updateUI(state: DetailViewModel.UiState) {
        // val waifu = state.waifu
        pbLoading.visibility = View.GONE
        state.url?.let {
            // tvDetail.text = it.imageId.toString()
            tvDetail.text = ""
            ivDetail.loadUrl(it)
            prepareDownload(it)
        }

        // val dominantColor = waifu.dominant_color
        // prepareDownload(waifu)
    }



    private fun prepareDownload(waifuUrl: String) {
        title = waifuUrl.substringAfterLast('/')
        link = waifuUrl
        imageExt = waifuUrl.substringAfterLast('.')
        // Toast.makeText(context, "Extension:$imageExt, Titulo:$title", Toast.LENGTH_SHORT).show()
    }

    private fun RequestPermision() {


        // private val storagePermissionChecker = PermissionChecker(, Manifest.permission.ACCESS_COARSE_LOCATION)
        viewLifecycleOwner.lifecycleScope.launch {
            // storagePermissionRequester.request()
            mainState.requestPermissionLauncher {isWritePermissionGranted = it}
            // requestDownload()
            /*if (isWritePermissionGranted == true) {
                requestDownload()
            }*/
        }



        /*when {
            ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED -> {
                if (isWritePermissionGranted == null) {
                    isWritePermissionGranted = true
                }
            }
            else -> {
                requestPermissionsLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (isWritePermissionGranted == null) {
                    isWritePermissionGranted = true
                }
                // Toast.makeText(applicationContext, "Vuelve a intentar la descarga", Toast.LENGTH_SHORT).show()

            }
        }*/
    }

    private fun FragmentDetailBinding.setUpElements() {
        fab.setOnClickListener {
            if (isWritePermissionGranted != true ) {
                RequestPermision()
                // Toast.makeText(requireContext(), "$isWritePermissionGranted", Toast.LENGTH_SHORT).show()
            }
            requestDownload()
        }
    }

    private fun requestDownload() {
        downloadImage(title!!, link!!, imageExt!!)
    }

    private fun doRequestPermission(isPermissionGranted:Boolean) {
        if (isPermissionGranted) {
            // Snackbar.make(binding.root, "Permission Granted!!", Snackbar.LENGTH_SHORT).show()
        } else {
            // Snackbar.make(binding.root, "Permission Denied!!", Snackbar.LENGTH_SHORT).show()
        }
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
                // Toast.makeText(context, "Flujo else", Toast.LENGTH_SHORT).show()
                return "image/jpeg"
            }
        }
    }

    private fun isGif(imagen: String): Boolean = imagen.endsWith("gif")


}

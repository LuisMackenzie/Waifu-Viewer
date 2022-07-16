package com.mackenzie.waifuviewer.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.WaifuViewModel
import com.mackenzie.waifuviewer.WaifuViewModelFactory
import com.mackenzie.waifuviewer.adapters.WaifuAdapter
import com.mackenzie.waifuviewer.adapters.WaifuPicsAdapter
import com.mackenzie.waifuviewer.data.WaifusRepository
import com.mackenzie.waifuviewer.databinding.FragmentWaifuBinding
import com.mackenzie.waifuviewer.ui.common.app
import com.mackenzie.waifuviewer.ui.common.launchAndCollect
import com.mackenzie.waifuviewer.ui.common.visible
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class WaifuFragment: Fragment(R.layout.fragment_waifu) {

    private val safeArgs: WaifuFragmentArgs by navArgs()
    private val viewModel: WaifuViewModel by viewModels {
        WaifuViewModelFactory(WaifusRepository(requireActivity().app)) }
    private lateinit var mainState: MainState
    private var mainServer: Boolean = false
    private val waifuAdapter = WaifuAdapter{mainState.onWaifuClicked(it)}
    private val waifuPicsAdapter = WaifuPicsAdapter{mainState.onWaifuPicsClicked(it)}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainState = buildMainState()
        mainServer = safeArgs.bundleInfo.getBoolean(IS_SERVER_SELECTED)
        val binding = FragmentWaifuBinding.bind(view).apply {
            if (mainServer) {
                recycler.adapter = waifuPicsAdapter
            } else {
                recycler.adapter = waifuAdapter
            }
        }

        // loadCustomResult()


        viewLifecycleOwner.launchAndCollect(viewModel.state) { binding.updateUI(it) }

        /*with(viewModel.state) {
            diff({it.waifusIm}, {waifuAdapter.waifuItemList = if (it != null) it.waifus else emptyList()})
            // diff({it.waifusPics}, {waifuPicsAdapter.waifuItemList = if (it != null) it else emptyList()})
            diff({it.isLoading}, {binding.progress.visible = isVisible})
        }*/

        loadCustomResult()


    }

    private fun loadCustomResult() {
        val bun = safeArgs.bundleInfo
        // mainServer = bun.getBoolean(IS_SERVER_SELECTED)
        val isNsfw = bun.getBoolean(IS_NSFW_WAIFU)
        val isGif = bun.getBoolean(IS_GIF_WAIFU)
        val orientation = bun.getBoolean(IS_LANDS_WAIFU)
        // val waifuId = bun.getString(ID_WAIFU)
        val categoryTag = bun.getString(CATEGORY_TAG)!!

        if (!mainServer) {
            when (categoryTag) {
                "uniform" -> {
                    viewModel.onCustomWaifusReady(isNsfw, isGif = false, categoryTag, orientation)
                }
                "maid" -> {
                    viewModel.onCustomWaifusReady(isNsfw, isGif = false, categoryTag, orientation)
                }
                "marin-kitagawa" -> {
                    viewModel.onCustomWaifusReady(isNsfw, isGif = false, categoryTag, orientation)
                }
                "mori-calliope" -> {
                    viewModel.onCustomWaifusReady(isNsfw, isGif = false, categoryTag, false)
                }
                "raiden-shogun" -> {
                    viewModel.onCustomWaifusReady(isNsfw, isGif = false, categoryTag, false)
                }
                "oppai" -> {
                    viewModel.onCustomWaifusReady(isNsfw, isGif = false, categoryTag, orientation)
                }
                else -> {
                    viewModel.onCustomWaifusReady(isNsfw, isGif, categoryTag, orientation)
                }
            }
        } else {
            when(categoryTag) {
                "all" -> {
                    viewModel.onCustomWaifusPicsReady(isNsfw, "waifu")
                }
                else -> {
                    viewModel.onCustomWaifusPicsReady(isNsfw, categoryTag)
                }
            }
        }
    }

    private fun FragmentWaifuBinding.updateUI(state: WaifuViewModel.UiState) {

        progress.visible = state.isLoading
        recycler.visibility = if(state.isLoading) View.GONE else View.VISIBLE
        ivError.visibility = if(state.isError) View.VISIBLE else View.GONE
        tvError.visibility = if(state.isError) View.VISIBLE else View.GONE
        state.waifusIm?.let { randomWaifus ->
            if (waifuAdapter.waifuItemList.isEmpty()) {
                waifuAdapter.waifuItemList = randomWaifus.waifus
                Toast.makeText(requireContext(), "Servidor WaifuIm", Toast.LENGTH_LONG).show()
            }
        }
        state.waifusPics?.let { randomWaifus ->
            if (waifuPicsAdapter.waifuItemList.isEmpty()) {
                waifuPicsAdapter.waifuItemList = randomWaifus
                Toast.makeText(requireContext(), "Servidor WaifuPics", Toast.LENGTH_LONG).show()
            }
        }
        /*state.waifusSavedPics?.let { randomWaifus ->
            if (waifuPicsAdapter.waifuItemList.isEmpty()) {
                waifuPicsAdapter.waifuItemList = randomWaifus
                Toast.makeText(requireContext(), "Local WaifuPics", Toast.LENGTH_LONG).show()
            }
        }*/
        /*state.waifusSavedIm?.let { randomWaifus ->
            if (waifuAdapter.waifuItemList.isEmpty()) {
                waifuAdapter.waifuItemList = randomWaifus
                Toast.makeText(requireContext(), "Local WaifuIm", Toast.LENGTH_LONG).show()
            }
        }*/
    }

    // Este metodo rebe un estado en un flow y devuelve algo
    private fun <T, U> Flow<T>.diff(mapf: (T) -> U, body: (U) -> Unit) {
        viewLifecycleOwner.launchAndCollect(
            flow = map(mapf).distinctUntilChanged(),
            body = body
        )
    }

    companion object {
        const val EXTRA_WAIFU = "WaifuFragment:waifu"
        const val IS_SERVER_SELECTED = "WaifuFragment:server"
        const val IS_NSFW_WAIFU = "WaifuFragment:nsfw"
        const val IS_GIF_WAIFU = "WaifuFragment:gif"
        const val IS_LANDS_WAIFU = "WaifuFragment:lands"
        const val ID_WAIFU = "WaifuFragment:id"
        const val CATEGORY_TAG = "WaifuFragment:tag"
        const val ORIENTATION_PORTRAIT = "PORTRAIT"
        const val ORIENTATION_LANDSCAPE = "LANDSCAPE"
        const val DEFAULT_REGION = "US"
    }

}


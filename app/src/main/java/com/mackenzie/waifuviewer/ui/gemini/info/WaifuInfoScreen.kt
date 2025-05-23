package com.mackenzie.waifuviewer.ui.gemini.info

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.BitmapImage
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.crossfade
import coil3.size.Precision
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.ui.common.GenerativeViewModelFactory
import com.mackenzie.waifuviewer.ui.common.UriSaver
import com.mackenzie.waifuviewer.ui.common.ui.isNavigationBarVisible
import com.mackenzie.waifuviewer.ui.theme.WaifuViewerTheme
import kotlinx.coroutines.launch

@Composable
internal fun WaifuInfoRoute(
    viewModel: WaifuInfoViewModel = viewModel(factory = GenerativeViewModelFactory)
) {
    val waifuInfoUiState by viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val imageRequestBuilder = ImageRequest.Builder(LocalContext.current)
    val imageLoader = ImageLoader.Builder(LocalContext.current).build()

    WaifuInfoScreen(
        uiState = waifuInfoUiState,
        onReasonClicked = { inputText, selectedItems ->
            coroutineScope.launch {
                val bitmaps = selectedItems.mapNotNull {
                    val imageRequest = imageRequestBuilder
                        .data(it)
                        // Scale the image down to 768px for faster uploads
                        .size(size = 768)
                        .precision(Precision.EXACT)
                        .build()
                    try {
                        val result = imageLoader.execute(imageRequest)
                        if (result is SuccessResult) {
                            return@mapNotNull (result.image as BitmapImage).bitmap
                        } else {
                            return@mapNotNull null
                        }
                    } catch (e: Exception) {
                        return@mapNotNull null
                    }
                }
                viewModel.reason(inputText, bitmaps)
            }
        }
    )
}

@Composable
fun WaifuInfoScreen(
    uiState: WaifuInfoUiState = WaifuInfoUiState.Loading,
    onReasonClicked: (String, List<Uri>) -> Unit = { _, _ -> }
) {
    var userQuestion by rememberSaveable { mutableStateOf("") }
    val imageUris = rememberSaveable(saver = UriSaver()) { mutableStateListOf() }

    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { imageUri ->
        imageUri?.let {
            imageUris.add(it)
        }
    }

    Box(
        modifier = if (isNavigationBarVisible()) {
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .background(MaterialTheme.colorScheme.background)
        } else {
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        }
    ) {

        AsyncImage(
            model= ImageRequest.Builder(LocalContext.current)
                .data("https://nekos.best/api/v2/neko/a534dc1a-0309-4e8b-983e-dd6b24231b58.png")
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_error_grey),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        Column(modifier = Modifier.padding(all = 16.dp).verticalScroll(rememberScrollState()) ,) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    IconButton(
                        onClick = {
                            pickMedia.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = stringResource(R.string.add_image),
                        )
                    }
                    OutlinedTextField(
                        value = userQuestion,
                        label = { Text(stringResource(R.string.reason_label)) },
                        placeholder = { Text(stringResource(R.string.reason_hint)) },
                        onValueChange = { userQuestion = it },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                    )
                    TextButton(
                        onClick = {
                            if (userQuestion.isNotBlank()) {
                                onReasonClicked(userQuestion, imageUris.toList())
                            }
                        },
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(stringResource(R.string.action_go))
                    }
                }
                LazyRow(
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    items(imageUris) { imageUri ->
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(4.dp)
                                .requiredSize(72.dp)
                        )
                    }
                }
            }
            when (uiState) {
                WaifuInfoUiState.Initial -> {
                    // Nothing is shown
                }

                WaifuInfoUiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is WaifuInfoUiState.Success -> {
                    Card(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(all = 16.dp)
                                .fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Outlined.Person,
                                contentDescription = "Person Icon",
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier
                                    .requiredSize(36.dp)
                                    .drawBehind {
                                        drawCircle(color = Color.White)
                                    }
                            )
                            Text(
                                text = uiState.outputText, // TODO(thatfiredev): Figure out Markdown support
                                color = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

                is WaifuInfoUiState.Error -> {
                    Card(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(all = 16.dp)
                        )
                    }
                }
            }
        }

    }
}

@Composable
@Preview(showSystemUi = true)
fun PhotoReasoningScreenPreview() {
    WaifuViewerTheme(darkTheme = true) {
        WaifuInfoScreen()
    }
}
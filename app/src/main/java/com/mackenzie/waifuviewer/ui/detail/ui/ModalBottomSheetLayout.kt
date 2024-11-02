package com.mackenzie.waifuviewer.ui.detail.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mackenzie.waifuviewer.domain.AnimeSearchItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetLayout(
    showBottomSheet: Boolean,
    searchResults: List<AnimeSearchItem>,
) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(showBottomSheet) }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
        ) {
            IconButton(
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) { showBottomSheet = false }
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
            SearchResultsBottomSheet(searchResults = searchResults)
        }
    }
}
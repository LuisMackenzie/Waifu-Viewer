package com.mackenzie.waifuviewer.ui.detail.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import com.mackenzie.waifuviewer.domain.AnimeSearchItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetLayout(
    showBottomSheet: Boolean,
    onDismissRequest: () -> Unit,
    searchResults: List<AnimeSearchItem>,
) {

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { onDismissRequest() },
            sheetState = SheetState(
                skipPartiallyExpanded = false,
                density = LocalDensity.current,
                initialValue = SheetValue.PartiallyExpanded,
                confirmValueChange = { newValue ->
                    when (newValue) {
                        SheetValue.Expanded -> {
                            return@SheetState true
                        }
                        SheetValue.PartiallyExpanded -> {
                            return@SheetState true
                        }
                        SheetValue.Hidden -> {
                            onDismissRequest()
                            return@SheetState true
                        }
                    }
                },
                skipHiddenState = false
            ),
        ) {
            SearchResultsBottomSheet(
                searchResults = searchResults
            )
        }
    }
}
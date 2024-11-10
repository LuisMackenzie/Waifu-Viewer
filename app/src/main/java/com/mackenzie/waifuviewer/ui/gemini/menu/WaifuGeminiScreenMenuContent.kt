package com.mackenzie.waifuviewer.ui.gemini.menu

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mackenzie.waifuviewer.domain.GeminiMenuItem
import com.mackenzie.waifuviewer.ui.common.ui.menuGenerator
import com.mackenzie.waifuviewer.ui.main.ui.MainThemeForPreview

@Composable
fun WaifuGeminiScreenMenuContent(
    menuItems: List<GeminiMenuItem> = menuGenerator(),
    onItemClicked: (String) -> Unit = { }
) {
    LazyColumn(
        Modifier
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        items(menuItems) { menuItem ->
            MenuItemView(
                menuItem = menuItem,
                onItemClicked = onItemClicked
            )
        }
    }
}



@Preview(showSystemUi = true)
@Composable
fun MenuScreenPreview() {
    MainThemeForPreview(darkTheme = false) {
        WaifuGeminiScreenMenuContent()
    }
}
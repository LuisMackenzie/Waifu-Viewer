package com.mackenzie.waifuviewer.ui.gemini

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.GeminiMenuItem
import com.mackenzie.waifuviewer.ui.common.ui.menuGenerator
import com.mackenzie.waifuviewer.ui.main.ui.MainTheme
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(menuItem.titleResId),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(menuItem.descriptionResId),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    TextButton(
                        onClick = {
                            onItemClicked(menuItem.routeId)
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = stringResource(R.string.action_try))
                    }
                }
            }
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
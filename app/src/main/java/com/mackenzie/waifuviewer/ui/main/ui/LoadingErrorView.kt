package com.mackenzie.waifuviewer.ui.main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.ui.detail.ui.LoadingAnimationError

@Preview(showBackground = true)
@Composable
fun LoadingErrorView(
    error: String = "Error Generico"
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        LoadingAnimationError()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hubo algun Error",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = error,
                fontSize = 28.sp,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
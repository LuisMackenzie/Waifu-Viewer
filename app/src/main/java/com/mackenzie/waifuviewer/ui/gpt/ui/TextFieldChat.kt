package com.mackenzie.waifuviewer.ui.gpt.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldChat(value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            // label = { Text("Label") },
            placeholder = { Text("Hint") },
            // trailingIcon = { Icons.Default.AcUnit },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = value,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        )
        Button(
            onClick = { onValueChange("") },
            modifier = Modifier.fillMaxWidth(),
            enabled = value.isNotEmpty()
        ) {
            Text("Clear")
        }
    }
}
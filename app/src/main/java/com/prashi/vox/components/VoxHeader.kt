package com.prashi.vox.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun VoxHeader() {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "VOX",
            color = Color(0xFF00E5FF),
            style = MaterialTheme.typography.displayLarge
        )

        Text(
            text = "AI ASSISTANT",
            color = Color(0xFF7FE8FF),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
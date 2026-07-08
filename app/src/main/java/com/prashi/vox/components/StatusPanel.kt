package com.prashi.vox.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatusPanel() {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text("🟢 AI ONLINE", color = Color(0xFF00E676))

        Spacer(modifier = Modifier.height(6.dp))

        Text("🟢 Internet Connected", color = Color(0xFF00E676))

        Spacer(modifier = Modifier.height(6.dp))

        Text("🟢 Weather Ready", color = Color(0xFF00E676))

        Spacer(modifier = Modifier.height(6.dp))

        Text("🟢 Reminder System Active", color = Color(0xFF00E676))
    }
}
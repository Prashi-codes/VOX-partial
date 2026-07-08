package com.prashi.vox.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ConversationPanel(
    text: String
) {

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .border(
                2.dp,
                Color(0xFF00E5FF),
                RoundedCornerShape(18.dp)
            )
            .background(
                Color(0xFF08111F),
                RoundedCornerShape(18.dp)
            )
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.verticalScroll(scrollState)
        ) {

            Text(
                text = "CONVERSATION",
                color = Color(0xFF00E5FF),
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
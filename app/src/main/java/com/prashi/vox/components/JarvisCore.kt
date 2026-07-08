package com.prashi.vox.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun JarvisCore(
    listening: Boolean
) {

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(250.dp)
    ) {

        Box(
            modifier = Modifier
                .size((220 * scale).dp)
                .border(2.dp, Color(0xFF00E5FF), CircleShape)
        )

        Box(
            modifier = Modifier
                .size((170 * scale).dp)
                .border(2.dp, Color(0xFF00BFFF), CircleShape)
        )

        Box(
            modifier = Modifier
                .size((90 * scale).dp)
                .background(Color(0xFF00E5FF), CircleShape)
        )

        Text(
            text = if (listening) "LISTENING" else "READY",
            color = Color.White
        )
    }
}
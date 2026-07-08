package com.prashi.vox.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun JarvisMenu(

    onHome: () -> Unit,

    onChat: () -> Unit,

    onTravel: () -> Unit,

    onNotes: () -> Unit,

    onWeather: () -> Unit,

    onNews: () -> Unit,

    onSettings: () -> Unit

) {

    Column(

        modifier = Modifier
            .fillMaxHeight()
            .width(260.dp)
            .background(Color(0xFF08111F))
            .padding(20.dp)

    ) {

        Text(
            "VOX",
            color = Color(0xFF00E5FF),
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            "AI ASSISTANT",
            color = Color.White
        )

        Spacer(modifier = Modifier.height(35.dp))

        MenuItem("🏠 Home", onHome)

        MenuItem("🤖 AI Chat", onChat)

        MenuItem("✈ AI Travel", onTravel)

        MenuItem("📝 Notes", onNotes)

        MenuItem("☁ Weather", onWeather)

        MenuItem("📰 News", onNews)

        MenuItem("⚙ Settings", onSettings)

        Spacer(modifier = Modifier.weight(1f))

        Text(
            "Version 1.0",
            color = Color.Gray
        )

    }

}

@Composable
private fun MenuItem(

    title: String,

    onClick: () -> Unit

) {

    Text(

        text = title,

        color = Color.White,

        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 14.dp)

    )

}
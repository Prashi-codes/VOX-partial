package com.prashi.vox.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.prashi.vox.components.ConversationPanel
import com.prashi.vox.components.JarvisCore
import com.prashi.vox.components.StatusPanel
import com.prashi.vox.components.VoxHeader

@Composable
fun HomeScreen(

    listening: Boolean,

    conversation: String

) {

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050B18))
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center

    ) {

        Spacer(modifier = Modifier.height(35.dp))

        VoxHeader()

        Spacer(modifier = Modifier.height(25.dp))

        JarvisCore(
            listening = listening
        )

        Spacer(modifier = Modifier.height(30.dp))

        ConversationPanel(
            text = conversation
        )

        Spacer(modifier = Modifier.height(15.dp))

        StatusPanel()

    }

}
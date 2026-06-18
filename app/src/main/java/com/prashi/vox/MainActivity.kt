package com.prashi.vox

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.compose.runtime.*
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.prashi.vox.ui.theme.VOXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VOXTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Prashanth KR",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    var spokenText by remember {
        mutableStateOf("Status: Ready")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "VOX")

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                spokenText = "Voice feature coming next..."
            }
        ) {
            Text("Talk to VOX")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = spokenText)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VOXTheme {
        Greeting("Android")
    }
}
package com.prashi.vox

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.*
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.util.*

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private lateinit var recognizer: SpeechRecognizer
    private lateinit var tts: TextToSpeech

    private val textState = mutableStateOf("VOX Ready")
    private val listeningState = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recognizer = SpeechRecognizer.createSpeechRecognizer(this)
        tts = TextToSpeech(this, this)

        setContent {
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text("VOX Assistant", style = MaterialTheme.typography.headlineMedium)

                Spacer(Modifier.height(20.dp))

                Button(onClick = { startListening() }) {
                    Text(if (listeningState.value) "Listening..." else "Talk to VOX")
                }

                Spacer(Modifier.height(20.dp))

                Text(textState.value)
            }
        }
    }

    override fun onInit(status: Int) {
        tts.language = Locale.US
        tts.setSpeechRate(0.9f)
    }

    private fun startListening() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            textState.value = "Mic permission required"
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        recognizer.setRecognitionListener(object : RecognitionListener {

            override fun onReadyForSpeech(params: Bundle?) {
                listeningState.value = true
                textState.value = "Listening..."
            }

            override fun onResults(results: Bundle?) {

                listeningState.value = false

                val text = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull() ?: ""

                textState.value = text

                if (text.isNotEmpty()) {
                    askAI(text)
                }
            }

            override fun onError(error: Int) {
                listeningState.value = false
                textState.value = "Try again"
            }

            override fun onEndOfSpeech() {
                listeningState.value = false
            }

            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        recognizer.startListening(intent)
    }

    private fun askAI(question: String) {

        Thread {
            try {

                val url = java.net.URL(
                    "https://api-inference.huggingface.co/models/google/flan-t5-small"
                )

                val conn = url.openConnection() as java.net.HttpURLConnection

                conn.requestMethod = "POST"

                conn.setRequestProperty(
                    "Authorization",
                    "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"
                )

                conn.setRequestProperty(
                    "Content-Type",
                    "application/json"
                )

                conn.connectTimeout = 15000
                conn.readTimeout = 15000
                conn.doOutput = true

                val body = """
                {
                  "inputs": "$question"
                }
            """.trimIndent()

                conn.outputStream.use {
                    it.write(body.toByteArray())
                }

                val code = conn.responseCode

                val response = if (code in 200..299) {
                    conn.inputStream.bufferedReader().readText()
                } else {
                    conn.errorStream?.bufferedReader()?.readText()
                        ?: "Unknown error"
                }

                runOnUiThread {

                    textState.value = "HTTP $code\n$response"

                    if (code == 200) {

                        try {

                            val arr = org.json.JSONArray(response)

                            val reply = arr
                                .getJSONObject(0)
                                .optString(
                                    "generated_text",
                                    "No response"
                                )

                            speak(reply)

                        } catch (e: Exception) {
                            speak("Response received")
                        }

                    } else {
                        speak("Server error")
                    }
                }

            } catch (e: Exception) {
                runOnUiThread {
                    textState.value = e.toString()
                }
            }

        }.start()
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "vox")
    }

    override fun onDestroy() {
        recognizer.destroy()
        tts.shutdown()
        super.onDestroy()
    }
}
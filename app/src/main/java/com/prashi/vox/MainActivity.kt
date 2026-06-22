package com.prashi.vox

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private lateinit var recognizer: SpeechRecognizer
    private lateinit var tts: TextToSpeech

    private var autoListen = true

    private val textState = mutableStateOf("VOX Ready")
    private val listeningState = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recognizer = SpeechRecognizer.createSpeechRecognizer(this)
        tts = TextToSpeech(this, this)

        Handler(mainLooper).postDelayed({
            startListening()
        }, 1500)

        setContent {

            MaterialTheme {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "VOX ASSISTANT",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(220.dp)
                    ) {

                        val infiniteTransition =
                            rememberInfiniteTransition()

                        val scale by infiniteTransition.animateFloat(
                            initialValue = 0.9f,
                            targetValue = 1.1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000),
                                repeatMode = RepeatMode.Reverse
                            )
                        )

                        Box(
                            modifier = Modifier
                                .size((180 * scale).dp)
                                .background(
                                    color = Color(0xFF00BFFF),
                                    shape = CircleShape
                                )
                        )

                        Text(
                            text = if (listeningState.value)
                                "LISTENING"
                            else
                                "VOX",
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        text = textState.value,
                        color = Color.White
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        autoListen = true

        Handler(mainLooper).postDelayed({
            startListening()
        }, 1000)
    }

    override fun onStop() {
        super.onStop()

        autoListen = false

        try {
            recognizer.stopListening()
        } catch (_: Exception) {
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
            tts.setSpeechRate(0.9f)
        }
    }

    private fun startListening() {

        if (listeningState.value) return

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            textState.value = "Microphone permission required"
            return
        }

        val intent = Intent(
            RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        ).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )
        }

        recognizer.setRecognitionListener(
            object : RecognitionListener {

                override fun onReadyForSpeech(params: Bundle?) {
                    listeningState.value = true
                    textState.value = "Listening..."
                }

                override fun onResults(results: Bundle?) {

                    listeningState.value = false

                    val text = results
                        ?.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                        )
                        ?.firstOrNull()
                        ?: ""

                    textState.value = text

                    if (text.isNotBlank()) {
                        handleInput(text)
                    }
                }

                override fun onError(error: Int) {

                    listeningState.value = false

                    if (autoListen) {
                        Handler(mainLooper).postDelayed({
                            startListening()
                        }, 1000)
                    }
                }

                override fun onEndOfSpeech() {
                    listeningState.value = false
                }

                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            }
        )

        recognizer.startListening(intent)
    }

    private fun handleInput(text: String) {

        val msg = text.lowercase()

        when {

            msg.contains("hello") ->
                speak("Hello Prashanth. How can I help you?")

            msg.contains("who are you") ->
                speak("I am VOX, your personal assistant.")

            msg.contains("time") -> {
                val time = SimpleDateFormat(
                    "hh:mm a",
                    Locale.getDefault()
                ).format(Date())

                speak("Current time is $time")
            }

            msg.contains("date") -> {
                val date = SimpleDateFormat(
                    "dd MMMM yyyy",
                    Locale.getDefault()
                ).format(Date())

                speak("Today is $date")
            }

            msg.contains("open youtube") ->
                openApp(
                    "com.google.android.youtube",
                    "YouTube"
                )

            msg.contains("open whatsapp") ->
                openApp(
                    "com.whatsapp",
                    "WhatsApp"
                )

            msg.contains("open chrome") ->
                openApp(
                    "com.android.chrome",
                    "Chrome"
                )

            else ->
                speak("Sorry, I don't know that command yet.")
        }
    }

    private fun openApp(
        packageName: String,
        appName: String
    ) {

        val intent =
            packageManager.getLaunchIntentForPackage(packageName)

        if (intent != null) {
            startActivity(intent)
            speak("Opening $appName")
        } else {
            speak("$appName is not installed")
        }
    }

    private fun speak(text: String) {

        tts.stop()

        tts.setOnUtteranceProgressListener(
            object : UtteranceProgressListener() {

                override fun onStart(utteranceId: String?) {}

                override fun onDone(utteranceId: String?) {

                    if (autoListen) {
                        Handler(mainLooper).postDelayed({
                            startListening()
                        }, 500)
                    }
                }

                override fun onError(utteranceId: String?) {}
            }
        )

        tts.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "VOX_REPLY"
        )
    }

    override fun onDestroy() {
        autoListen = false
        recognizer.destroy()
        tts.shutdown()
        super.onDestroy()
    }
}
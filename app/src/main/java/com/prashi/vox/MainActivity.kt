package com.prashi.vox

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.remember
import com.prashi.vox.components.JarvisMenu
import androidx.compose.runtime.mutableStateOf
import com.prashi.vox.components.VoxHeader
import com.prashi.vox.components.JarvisCore
import com.prashi.vox.components.ConversationPanel
import com.prashi.vox.components.StatusPanel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*
import com.prashi.vox.ai.GroqManager
import com.prashi.vox.news.NewsManager
import com.prashi.vox.reminder.ReminderManager
import com.prashi.vox.weather.WeatherManager
import androidx.navigation.compose.rememberNavController
import com.prashi.vox.navigation.AppNavigation

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private lateinit var recognizer: SpeechRecognizer
    private lateinit var tts: TextToSpeech
    private lateinit var groqManager: GroqManager
    private var autoListen = true

    private val textState = mutableStateOf("VOX Ready")
    private val listeningState = mutableStateOf(false)
    private val isTyping = mutableStateOf(false)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var newsManager: NewsManager
    private lateinit var weatherManager: WeatherManager
    private lateinit var reminderManager: ReminderManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recognizer = SpeechRecognizer.createSpeechRecognizer(this)
        tts = TextToSpeech(this, this)
        Handler(mainLooper).postDelayed({
            tts.speak(
                "Voice test",
                TextToSpeech.QUEUE_FLUSH,
                null,
                "TEST"
            )
        }, 3000)
        newsManager = NewsManager()
        weatherManager = WeatherManager()
        reminderManager = ReminderManager()
        groqManager = GroqManager()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                102
            )
        }
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)
        Handler(mainLooper).postDelayed({
            startListening()
        }, 1500)

        setContent {

            MaterialTheme {

                val menuOpen = remember { mutableStateOf(false) }
                val navController = rememberNavController()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF050B18))
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {

                            IconButton(
                                onClick = {
                                    menuOpen.value = !menuOpen.value
                                }
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = Color(0xFF00E5FF)
                                )

                            }

                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        AppNavigation(
                            navController = navController,
                            listening = listeningState.value,
                            conversation = textState.value
                        )

                    }

                    AnimatedVisibility(
                        visible = menuOpen.value,
                        enter = slideInHorizontally(
                            initialOffsetX = { -it }
                        ),
                        exit = slideOutHorizontally(
                            targetOffsetX = { -it }
                        )
                    ) {

                        JarvisMenu(

                            onHome = {
                                menuOpen.value = false
                                navController.navigate("home")
                            },

                            onChat = {
                                menuOpen.value = false
                                navController.navigate("chat")
                            },

                                    onTravel = {
                                menuOpen.value = false
                                navController.navigate("travel")
                            },

                                    onNotes = {
                                menuOpen.value = false
                                navController.navigate("notes")
                            },

                                    onWeather = {
                                menuOpen.value = false
                                navController.navigate("weather")
                            },

                                    onNews = {
                                menuOpen.value = false
                                navController.navigate("news")
                            },

                                    onSettings = {
                                menuOpen.value = false
                                navController.navigate("settings")
                            }

                        )

                    }

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

            val result = tts.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {

                textState.value = "TTS language not supported"
            }

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
    private fun getCurrentLocationWeather() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            textState.value = "Location permission required"

            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->

                if (location != null) {

                    val latitude = location.latitude
                    val longitude = location.longitude

                    weatherManager.getWeather(

                        latitude,
                        longitude,

                        onSuccess = { weather ->

                            runOnUiThread {

                                textState.value = weather
                                speak(weather)
                            }
                        },

                        onError = { error ->

                            runOnUiThread {

                                textState.value = error
                                speak("Unable to fetch weather")
                            }
                        }
                    )

                } else {
                    speak("Unable to get your location")
                }
            }
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
            msg.contains("weather") ->
                getCurrentLocationWeather()
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
            msg.contains("today's news") ||
                    msg.contains("todays news") ||
                    msg.contains("news") ||
                    msg.contains("ಸುದ್ದಿ") ->
                newsManager.getKannadaNews(

                    onSuccess = { news ->

                        runOnUiThread {

                            textState.value = news
                            speakKannada(news)
                        }
                    },

                    onError = { error ->

                        runOnUiThread {

                            textState.value = error
                            speakKannada("ಸುದ್ದಿ ಪಡೆಯಲು ಸಾಧ್ಯವಾಗಲಿಲ್ಲ")
                        }
                    }
                )
            msg.startsWith("remind me to") &&
                    msg.contains("in") &&
                    (msg.contains("minute") || msg.contains("minutes")) -> {

                try {

                    val task = msg
                        .substringAfter("remind me to")
                        .substringBefore("in")
                        .trim()

                    val minutes = Regex("\\d+")
                        .find(msg.substringAfter("in"))
                        ?.value
                        ?.toInt() ?: 0

                    if (minutes > 0) {

                        val triggerTime =
                            System.currentTimeMillis() +
                                    (minutes * 60 * 1000)

                        reminderManager.scheduleReminder(
                            context = this,
                            message = task,
                            triggerTime = triggerTime
                        )

                        speak(
                            "Reminder set to $task in $minutes minutes"
                        )

                    } else {
                        speak("Please tell me the number of minutes")
                    }

                } catch (e: Exception) {

                    speak(
                        "Sorry, I could not understand the reminder"
                    )
                }
            }
            else -> {
                isTyping.value = true
                textState.value = "Thinking..."
                groqManager.askAI(

                    question = text,

                    onSuccess = { answer ->

                        runOnUiThread {

                            textState.value = answer
                            speak(answer)
                        }
                    },

                    onError = { error ->

                        runOnUiThread {

                            textState.value = error
                            speak("Sorry, I couldn't reach the AI.")
                        }
                    }
                )
            }
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
    private fun speakKannada(text: String) {

        tts.language = Locale("kn", "IN")

        tts.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "VOX_KANNADA"
        )

        tts.language = Locale.US
    }
    override fun onDestroy() {
        autoListen = false
        recognizer.destroy()
        tts.shutdown()
        super.onDestroy()
    }
    private fun animateAnswer(answer: String) {

        Thread {

            val builder = StringBuilder()

            for (c in answer) {

                builder.append(c)

                runOnUiThread {
                    textState.value = builder.toString()
                }

                Thread.sleep(15)
            }

            runOnUiThread {
                speak(answer)
            }

        }.start()
    }
}
package com.prashi.vox

import android.app.*
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import java.text.SimpleDateFormat
import java.util.*

class VoiceService : Service(), TextToSpeech.OnInitListener {

    private lateinit var recognizer: SpeechRecognizer
    private lateinit var tts: TextToSpeech

    private var isSpeaking = false

    override fun onCreate() {
        super.onCreate()

        tts = TextToSpeech(this, this)
        recognizer = SpeechRecognizer.createSpeechRecognizer(this)

        startForeground(1, createNotification())
        startListening()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // NO AUTO RESTART LOOP
        return START_NOT_STICKY
    }

    // 🎤 LISTEN ONLY WHEN REQUESTED
    private fun startListening() {

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.getDefault())
        }

        recognizer.setRecognitionListener(object : RecognitionListener {

            override fun onReadyForSpeech(params: Bundle?) {}

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}

            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {
                // DO NOTHING (no auto restart)
            }

            override fun onResults(results: Bundle?) {

                val text = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    ?.lowercase()
                    ?: ""

                handleCommand(text)
            }
        })

        recognizer.startListening(intent)
    }

    // 🧠 COMMANDS
    private fun handleCommand(msg: String) {

        when {

            "hello" in msg -> speak("Hello, I am VOX")

            "time" in msg -> {
                val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
                speak("Time is $time")
            }

            "date" in msg -> {
                val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                speak("Today is $date")
            }

            "open youtube" in msg -> {
                val intent = packageManager.getLaunchIntentForPackage("com.google.android.youtube")
                if (intent != null) startActivity(intent)
                speak("Opening YouTube")
            }

            else -> speak("I heard you say $msg")
        }
    }

    // 🔊 SPEAK (NO LOOPING MIC)
    private fun speak(text: String) {

        isSpeaking = true

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "vox")

        tts.setOnUtteranceProgressListener(object :
            android.speech.tts.UtteranceProgressListener() {

            override fun onStart(utteranceId: String?) {}

            override fun onDone(utteranceId: String?) {
                isSpeaking = false
                startListening() // ONLY AFTER SPEECH
            }

            override fun onError(utteranceId: String?) {
                isSpeaking = false
            }
        })
    }

    // 🔔 NOTIFICATION
    private fun createNotification(): Notification {

        val channelId = "vox_channel"

        val channel = NotificationChannel(
            channelId,
            "VOX Running",
            NotificationManager.IMPORTANCE_LOW
        )

        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)

        return Notification.Builder(this, channelId)
            .setContentTitle("VOX Active")
            .setContentText("Tap app to use VOX")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .build()
    }

    override fun onDestroy() {
        recognizer.destroy()
        tts.shutdown()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onInit(status: Int) {}
}